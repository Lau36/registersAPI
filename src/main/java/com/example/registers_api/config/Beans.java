package com.example.registers_api.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
    private static final String SERVER_URL = "http://localhost:8181/";
    private static final String REALM_MASTER = "master";
    private static final String ADMIN_CLI = "admin-cli";
    private static final String USERNAME = "admin"; // Admin console user
    private static final String PASSWORD = "admin";

    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .clientId(ADMIN_CLI)
                .username(USERNAME)
                .password(PASSWORD)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

}
