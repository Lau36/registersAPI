package com.example.registers_api.utils;

import org.bson.types.ObjectId;

public class ObjectIdMapper {

    public ObjectId toObjectId(String idStr) {
        if (!ObjectId.isValid(idStr)) {
            throw new IllegalArgumentException("El id no es un ObjectId válido: " + idStr);
        }
        return new ObjectId(idStr);
    }
}
