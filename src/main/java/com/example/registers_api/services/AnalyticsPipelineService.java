package com.example.registers_api.services;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsPipelineService {

    private final MongoTemplate mongoTemplate;

    public AnalyticsPipelineService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    /**
     * Ejecuta un pipeline de agregación específico para un solo documento de 'registers'
     * y agrega las filas resultantes a la vista materializada 'analytics_register'.
     *
     * @param registerId El ID del documento que acaba de ser creado o actualizado.
     */
    public void updateAnalyticsForSingleRegister(String registerId) {

        // --- PASO 1: LA ETAPA MÁS IMPORTANTE: $match ---
        // Filtramos para procesar ÚNICAMENTE el documento que nos interesa.
        MatchOperation matchStage = Aggregation.match(Criteria.where("_id").is(registerId));

        // Etapas para aplanar los datos (igual que antes)
        UnwindOperation unwindLayers = Aggregation.unwind("registerInfo");
        UnwindOperation unwindVariables = Aggregation.unwind("registerInfo.variables");

        // Etapa para construir el nuevo formato (el "estampado" completo)
        ProjectionOperation projectToAnalyticsFormat = Aggregation.project()
                .and("_id").as("patientRegisterId")
                .and("updateRegisterDate").as("readingTimestamp")
                .and("registerInfo.variables.name").as("variableName")
                .and("registerInfo.variables.valueAsNumber").as("valueNumeric")
                .and("registerInfo.variables.valueAsString").as("valueText")
                .and("patientBasicInfo.name").as("patient_name")
                .and("patientBasicInfo.age").as("patient_ageAtReading")
                .and("patientBasicInfo.maritalStatus").as("patient_maritalStatus")
                .and("caregiver.name").as("caregiver_name")
                .and("registerInfo.researchLayerName").as("researchLayerName")
                // ... y así para TODAS las demás columnas que definimos ...
                ;

        // --- PASO 2: LA ETAPA $merge (CLAVE PARA "IR AGREGANDO") ---
        // Esta configuración asegura que solo se inserten filas nuevas.
//        MergeOperation mergeIntoAnalytics = Aggregation.merge()
//                .into(MergeTarget.of("analytics_register"))
//                // Definimos una clave única para cada "fila" analítica para evitar duplicados accidentales
//                .on("patientRegisterId", "readingTimestamp", "variableName")
//                .whenMatchedReplace() // Si por alguna razón se vuelve a procesar, la reemplaza
//                .whenNotMatchedInsert() // ¡Esta es la opción clave! Si no existe, la inserta.
//                .build();
//
//        // Construimos y ejecutamos el pipeline
//        Aggregation pipeline = Aggregation.newAggregation(
//                matchStage, // <-- ¡El filtro va primero!
//                unwindLayers,
//                unwindVariables,
//                projectToAnalyticsFormat,
//                mergeIntoAnalytics
//        );
//
//        // El pipeline corre sobre 'registers' pero solo procesa un documento.
//        mongoTemplate.aggregate(pipeline, "registers", Object.class);
    }
}
