package com.example.registers_api.repository;

import com.example.registers_api.models.RegisterCollection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RegisterRepository extends MongoRepository<RegisterCollection, String> {
    List<RegisterCollection> findAllBy(PageRequest pageRequest);
}
