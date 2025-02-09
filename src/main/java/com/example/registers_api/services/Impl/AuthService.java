package com.example.registers_api.services.Impl;

import com.example.registers_api.dtos.AuthDTO;
import com.example.registers_api.services.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String KEYCLOAK_SERVER_URL = "http://localhost:8181/realms/registeUsersApiDev/protocol/openid-connect";
    private final String clientId = "registers-users-api-rest";
    private final String clientSecret = "it9kVcNXDEYXoLnbR3ygY8QzaEgTQAw5";

    @Override
    public ResponseEntity<Map> login(AuthDTO auth) {
        String tokenUrl = KEYCLOAK_SERVER_URL + "/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = addBody(clientId, clientSecret);
        body.add("grant_type", "password");
        body.add("username", auth.getEmail());
        body.add("password", auth.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);
    }

    @Override
    public ResponseEntity<String> logout(String refreshToken) {
        String logoutUrl = KEYCLOAK_SERVER_URL + "/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = addBody(clientId, clientSecret);
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(logoutUrl, request, String.class);

        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }

    @Override
    public ResponseEntity<Map> refreshToken(String refreshToken) {
        String tokenUrl = KEYCLOAK_SERVER_URL + "/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = addBody(clientId, clientSecret);
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);
    }

    private MultiValueMap<String, String> addBody(String client_id, String client_secret) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", client_id);
        body.add("client_secret", client_secret);
        return body;
    }
}
