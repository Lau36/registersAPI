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
        IndexOperations ops = mongoTemplate.indexOps("analytics_register");
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

        boolean idEsObjectId = ObjectId.isValid(registerId);
        Document idMatch = new Document("_id", idEsObjectId ? new ObjectId(registerId) : registerId);
        pipeline.add(new Document("$match", idMatch));

        pipeline.add(new Document("$unwind", "$registerInfo"));

        if (researchLayerId != null) {
            pipeline.add(new Document("$match", new Document("registerInfo.researchLayerId", researchLayerId)));
        }

        pipeline.add(new Document("$unwind", "$registerInfo.variables"));

        // ✅ timestamps del servidor
        pipeline.add(new Document("$addFields", new Document()
                .append("snapshotAt", "$$NOW")
                .append("createdAt",  "$$NOW")
        ));

        final String variablePath = "$registerInfo.variables._id"; // usa "._id" si tu doc realmente lo guarda así

        Document project = new Document("$project", new Document()
                .append("_id", 0) // evita colisiones del _id original

                // base
                .append("registerId", "$_id")
                .append("patientIdentificationNumber", "$patientIdentificationNumber")
                .append("patientIdentificationType", "$patientIdentificationType")

                // capa
                .append("researchLayerId", "$registerInfo.researchLayerId")
                .append("researchLayerName", "$registerInfo.researchLayerName")

                // variable
                .append("variableId",   variablePath)
                .append("variableName", "$registerInfo.variables.name")
                .append("variableType", "$registerInfo.variables.type")
                .append("valueAsString","$registerInfo.variables.valueAsString")
                .append("valueAsNumber","$registerInfo.variables.valueAsNumber")

                // patient plano
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

                // caregiver plano
                .append("caregiverName", "$caregiver.name")
                .append("caregiverIdentificationType", "$caregiver.identificationType")
                .append("caregiverIdentificationNumber", "$caregiver.identificationNumber")
                .append("caregiverAge", "$caregiver.age")
                .append("caregiverEducationLevel", "$caregiver.educationLevel")
                .append("caregiverOccupation", "$caregiver.occupation")

                // timestamps ya añadidos
                .append("snapshotAt", "$snapshotAt")
                .append("createdAt",  "$createdAt")
        );
        pipeline.add(project);

        // robustez: descartar sin variableId
        pipeline.add(new Document("$match", new Document("variableId", new Document("$ne", null))));

        Document merge = new Document("$merge", new Document()
                .append("into", DST)
                .append("on", Arrays.asList("patientIdentificationNumber","researchLayerId","variableId","snapshotAt"))
                .append("whenMatched", "keepExisting")
                .append("whenNotMatched", "insert")
        );
        pipeline.add(merge);

        mongoTemplate.getDb().getCollection(SRC).aggregate(pipeline).toCollection();
    }
}
