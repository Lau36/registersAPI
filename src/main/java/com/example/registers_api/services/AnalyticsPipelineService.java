package com.example.registers_api.services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AnalyticsPipelineService {

    private final MongoTemplate mongoTemplate;

    private static final String SRC = "registers";
    private static final String DST = "analytics_register";

    // ---------- Índice único acorde a la clave del $merge ----------
    @PostConstruct
    public void ensureIndexes() {
        IndexOperations ops = mongoTemplate.indexOps(DST);
        ops.ensureIndex(new Index()
                .on("patientIdentificationNumber", Sort.Direction.ASC)
                .on("researchLayerId", Sort.Direction.ASC)
                .on("variableId", Sort.Direction.ASC)
                .on("snapshotAt", Sort.Direction.ASC)
                .unique());
    }

    // ====== 1) Creación inicial: todas las capas ======
    public void insertInitialSnapshot(String registerId) {
        runAggregation(registerId, null); // null -> procesa todas las capas
    }

    // ====== 2) Cambio en una capa: solo esa capa ======
    public void insertLayerSnapshot(String registerId, String researchLayerId) {
        runAggregation(registerId, researchLayerId);
    }

    // ====== 3) Cambio en patient/caregiver: todas las capas ======
    public void insertAllLayersSnapshot(String registerId) {
        runAggregation(registerId, null);
    }

    // ---------- Pipeline base: “siempre insertar, nunca sobrescribir” ----------
    @SuppressWarnings("unchecked")
    private void runAggregation(String registerId, String researchLayerId) {
        List<Document> pipeline = new ArrayList<>();

        // 1) Match por registro
        boolean idEsObjectId = ObjectId.isValid(registerId);
        Document idMatch = new Document("_id", idEsObjectId ? new ObjectId(registerId) : registerId);
        pipeline.add(new Document("$match", idMatch));

        // 2) Flatten de capas
        pipeline.add(new Document("$unwind", "$registerInfo"));

        // 2b) Si se indicó capa, filtrar de nuevo tras el unwind
        if (researchLayerId != null) {
            pipeline.add(new Document("$match", new Document("registerInfo.researchLayerId", researchLayerId)));
        }

        // 3) Flatten de variables
        pipeline.add(new Document("$unwind", "$registerInfo.variables"));

        // 4) Proyección PLANA + sellado de tiempo (versionador)
        Document project = new Document("$project", new Document()
                // Versionador para que NUNCA haya conflicto (cada corrida inserta nuevas filas)
                .append("snapshotAt", new Date())

                // Identificadores base
                .append("registerId", "$_id")
                .append("patientIdentificationNumber", "$patientIdentificationNumber")
                .append("patientIdentificationType", "$patientIdentificationType")

                // Capa
                .append("researchLayerId", "$registerInfo.researchLayerId")
                .append("researchLayerName", "$registerInfo.researchLayerName")

                // Variable
                .append("variableId", "$registerInfo.variables._id") // si en tu mapper usas 'id' cambia a ".id"
                .append("variableName", "$registerInfo.variables.name")
                .append("variableType", "$registerInfo.variables.type")
                .append("valueAsString", "$registerInfo.variables.valueAsString")
                .append("valueAsNumber", "$registerInfo.variables.valueAsNumber")

                // PatientBasicInfo (plano)
                .append("patientName", "$patientBasicInfo.name")
                .append("patientSex", "$patientBasicInfo.sex")
                .append("patientBirthDate", "$patientBasicInfo.birthDate")
                .append("patientAge", "$patientBasicInfo.age")
                .append("patientEmail", "$patientBasicInfo.email")
                .append("patientPhoneNumber", "$patientBasicInfo.phoneNumber")
                .append("patientDeathDate", "$patientBasicInfo.deathDate")
                .append("patientEconomicStatus", "$patientBasicInfo.economicStatus")
                .append("patientEducationLevel", "$patientBasicInfo.educationLevel")
                .append("patientMaritalStatus", "$patientBasicInfo.maritalStatus")
                .append("patientHometown", "$patientBasicInfo.hometown")
                .append("patientCurrentCity", "$patientBasicInfo.currentCity")
                .append("patientFirstCrisisDate", "$patientBasicInfo.firstCrisisDate")
                .append("patientCrisisStatus", "$patientBasicInfo.crisisStatus")

                // Caregiver (plano)
                .append("caregiverName", "$caregiver.name")
                .append("caregiverIdentificationType", "$caregiver.identificationType")
                .append("caregiverIdentificationNumber", "$caregiver.identificationNumber")
                .append("caregiverAge", "$caregiver.age")
                .append("caregiverEducationLevel", "$caregiver.educationLevel")
                .append("caregiverOccupation", "$caregiver.occupation")

                // Marcas de tiempo útiles para BI
                .append("createdAt", new Date())
        );
        pipeline.add(project);

        // 5) MERGE: clave incluye snapshotAt para garantizar unicidad SIEMPRE
        Document merge = new Document("$merge", new Document()
                .append("into", DST)
                .append("on", Arrays.asList(
                        "patientIdentificationNumber",
                        "researchLayerId",
                        "variableId",
                        "snapshotAt" // <-- clave versionadora
                ))
                .append("whenMatched", "keepExisting") // nunca tocar versiones previas
                .append("whenNotMatched", "insert")
        );
        pipeline.add(merge);

        // 6) Ejecutar
        mongoTemplate.getDb().getCollection(SRC).aggregate(pipeline).toCollection();
    }
}
