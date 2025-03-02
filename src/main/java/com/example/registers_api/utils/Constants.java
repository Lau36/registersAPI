package com.example.registers_api.utils;

public class Constants {
    private Constants() {
    throw new IllegalStateException("Utility class");
}

    public static final String RESEARCH_LAYER_CREATED = "Se creó la capa de investigación correctamente";
    public static final String VARIABLE_CREATED = "Se creó la variable correctamente";
    public static final String RESEACH_LAYER_NOT_CREATED = "Ocurrió un error al crear la capa de investigación";
    public static final String REGISTER_CREATED = "Registro creado con éxito";

    public static final String ADMIN_ROLE = "Admin_client_role";
    public static final String DOCTOR_ROLE = "Doctor_client_role";
    public static final String RESEARCHER_ROLE = "Researcher_client_role";

    public static final String ADMIN = "Admin";
    public static final String DOCTOR = "Doctor";
    public static final String RESEARCHER = "Researcher";

    public static final String REALM_NAME = "registeUsersApiDev";

    public static final String DOCUMENT_TYPE = "documentType";
    public static final String DOCUMENT_NUMBER = "documentNumber";
    public static final String RESEARCH_LAYER = "researchLayerId";
    public static final String BIRTHDATE = "birthDate";


    public static final String USER_CREATED = "Usuario creado exitosamente";
    public static final String USER_UPDATED = "Usuario actualizado exitosamente";
    public static final String USER_DELETED = "Usuario eliminado exitosamente";
    public static final String ERROR_WITH_USER_CREATED = "Error al crear usuario: ";
    public static final String DATE = "yyyy-MM-dd";

    public static final Integer STATUS_CODE_201 = 201;

    public static final String ERROR_CREATING_USER = "Ocurrió un error, no se pudo crear el usuario";
    public static final String ERROR_WITH_KEYCLOAK = "Error con servicio externo";
    public static final String TOKEN = "/token";
    public static final String GRANT_TYPE = "grant_type";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String LOGOUT = "/logout";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String SESION_CLOSED = "Sesión cerrada exitosamente";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";

    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String ERROR_PROCESING_RESPONSE = "Error al procesar la respuesta del servidor";




}
