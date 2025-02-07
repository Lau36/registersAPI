package com.example.registers_api.utils;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;


public class KeycloakProvider {

//    private static final String SERVER_URL = "http://localhost:8080/auth";
//    private static final String REALM_NAME = "registeUsersApiDev";
//    private static final String REALM_MASTER = "master";
//    private static final String ADMIN_CLI = "admin-cli";
//    private static final String USERNAME = "admin"; // Admin console user
//    private static final String PASSWORD = "admin"; // Admin console password
//
//    @Bean
//    public Keycloak getKeycloak() {
//        return KeycloakBuilder.builder()
//                .serverUrl("http://localhost:8080/auth")
//                .realm("master")
//                .clientId("admin-cli")
//                .username("admin")
//                .password("admin")
//                .grantType("password")
//                .build();
//    }

}