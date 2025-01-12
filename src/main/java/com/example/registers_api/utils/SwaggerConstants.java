package com.example.registers_api.utils;

public class SwaggerConstants {
    public static final String CREATE_RESEARCH_LAYER = "Crear una capa de investigación";
    public static final String CREATE_RESEARCH_LAYER_DESCRIPTION = "Servicio para crear una capa de investigación";
    public static final String GET_RESEARCH_LAYERS = "Obtener todas las capas de investigación";
    public static final String GET_RESEARCH_LAYERS_DESCRIPTION = "Servicio en donde se obtienen todas las capas de investigación existentes";

    public static final String GET_RESEARCH_LAYER = "Obtener una capa de investigación";
    public static final String GET_RESEARCH_LAYER_DESCRIPTION = "Servicio en donde se obtiene solo una capa de investigación con el id de ella";

    public static final String EMPTY_FIELDS = "No se pueden ingresar valores vacios";
    public static final String RESEARCH_LAYER_NAME_ALREADY_EXISTS = "El nombre ' ' ya existe";
    public static final String RESEARCH_LAYER_NAME_TOO_LONG = "El nombre excede el limite de los 100 caracteres";
    public static final String RESEARCH_LAYER_DESCRIPTION_TOO_LONG = "La descripción excede el limite de los 200 caracteres";

    public static final String CREATE_VARIABLE = "Crear una capa de investigación";
    public static final String CREATE_VARIABLE_DESCRIPTION = "Servicio para crear una capa de investigación";

    public static final String GET_VARIABLES = "Obtener todas las variables";
    public static final String GET_VARIABLE_DESCRIPTION = "Servicio en donde se obtienen todas las variables existentes";

    public static final String GET_VARIABLES_BY_RESEARCH_LAYER_ID = "Obtener todas las variables de una capa de investigación";
    public static final String GET_VARIABLES_BY_RESEARCH_LAYER_ID_DESCRIPTION = "Servicio que por medio del id de la capa de investigación obtienen todas las variables que están asociadas a esta";

    public static final String VARIABLE_NAME_ALREADY_EXISTS = "El nombre ' ' ya existe";
    public static final String VARIABLE_NAME_TOO_LONG = "El nombre excede el limite de los 100 caracteres";
    public static final String VARIABLE_DESCRIPTION_TOO_LONG = "La descripción excede el limite de los 200 caracteres";
    public static final String VARIABLE_RESEARCH_LAYER_ID_DOESNT_EXISTS = "El id de esa capa de investigación no existe";
}
