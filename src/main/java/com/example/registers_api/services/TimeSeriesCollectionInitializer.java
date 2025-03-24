package com.example.registers_api.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.bson.Document;


//@Profile("!test")//evita que se ejecute en los test
@Component
@RequiredArgsConstructor
public class TimeSeriesCollectionInitializer {
//    private final MongoTemplate mongoTemplate;
//
//    @PostConstruct
//    public void createTimeSeriesCollectionIfNotExists() {
//        String collectionName = "registers";
//
//        if (!mongoTemplate.collectionExists(collectionName)) {
//            Document timeSeriesOptions = new Document()
//                    .append("timeField", "registerDate")
//                    .append("metaField", "patientIdentificationNumber")
//                    .append("granularity", "hours");
//
//            Document command = new Document()
//                    .append("create", collectionName)
//                    .append("timeseries", timeSeriesOptions);
//
//            mongoTemplate.execute(db -> {
//                db.runCommand(command);
//                return null;
//            });
//
//            System.out.println("✅ Colección time-series creada: " + collectionName);
//        }
//    }
}
