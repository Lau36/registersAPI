package com.example.registers_api.utils;

public class ExceptionConstants {
    private ExceptionConstants() {
        throw new IllegalStateException("Utility class");
    }
    public static final String NOT_EMPTY_FIELDS = "No se pueden ingresar valores vacios";
    public static final String ALREADY_RESEARCH_LAYER_NAME_EXIST_EXCEPTION = "Ya existe una capa de investigación con el name '%s'";
    public static final String ALREADY_VARIABLE_NAME_EXIST_EXCEPTION = "Ya existe una variable con el name '%s'";
    public static final String LAYER_NAME_LENGTH_EXCEEDED = "El name de la capa de investigación no puede exceder los 100 caracteres";
    public static final String LAYER_DESCRIPTION_LENGTH_EXCEEDED = "La descripción de la capa de investigación no puede exceder los 250 caracteres";
    public static final String MAX_LENGTH_EXCEEDED = "El campo %s no puede exceder los %s caracteres";
    public static final String DOESNT_EXIST = "El campo %s no existe";
    public static final String REGISTER_NOT_FOUND = "No existe un registro con el id: '%s'";
    public static final String VARIABLE_NAME_NOT_FOUND = "No existe una variable con el name: '%s'";
    public static final String VARIABLE_ID_NOT_FOUND = "No existe una variable con el id: '%s'";
    public static final String RESEARCH_LAYER_ID_NOT_FOUND = "No existe una capa de investigación con el id: '%s'";
    public static final String RESEARCH_LAYER_NAME_NOT_FOUND = "No existe una capa de investigación con el name: '%s'";
    public static final String NOT_EMPTY_HEALTH_PROFESIONAL_FIELD = "El campo de profesional de salud no puede estar vacio o con campos nulos";
    public static final String NOT_EMPTY_VARIABLES = "Las variables no pueden estar vacias o con campos nulos";
    public static final String DOESNT_HAVE_PERMISSIONS = "No se puede realizar el registro porque no pertenece a esa capa de investigación";
}
