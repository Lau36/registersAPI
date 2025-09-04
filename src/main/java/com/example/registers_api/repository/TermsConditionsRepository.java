package com.example.registers_api.repository;

import com.example.registers_api.models.TermsAndConditionsCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TermsConditionsRepository extends MongoRepository<TermsAndConditionsCollection, String> {
}

