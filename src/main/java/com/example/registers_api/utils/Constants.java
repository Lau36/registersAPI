package com.example.registers_api.utils;

import java.time.LocalDate;

public class Constants {
    private Constants() {
    throw new IllegalStateException("Utility class");
}

    public static final String RESEARCH_LAYER_CREATED = "Se creó la capa de investigación correctamente";
    public static final String VARIABLE_CREATED = "Se creó la variable correctamente";
    public static final String RESEACH_LAYER_NOT_CREATED = "Ocurrió un error al crear la capa de investigación";

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



}
