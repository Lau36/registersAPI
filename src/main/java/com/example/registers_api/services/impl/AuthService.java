package com.example.registers_api.services.impl;

import com.example.registers_api.dtos.AuthDTO;
import com.example.registers_api.services.IAuthService;
import com.example.registers_api.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.example.registers_api.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String KEYCLOAK_SERVER_URL = "http://keycloak:8181/realms/registeUsersApiDev/protocol/openid-connect/token";
    private static final String CLIENT_ID = "registers-users-api-rest";
    private static final String CLIENT_SECRET = "D8u3ColBJDAjfxkxwxy2v4DPH5ftmgKx";

    @Override
    public ResponseEntity<Map> login(AuthDTO auth) {
        String tokenUrl = KEYCLOAK_SERVER_URL + TOKEN;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = addBody();
        body.add(GRANT_TYPE, PASSWORD);
        body.add(USERNAME, auth.getEmail());
        body.add(PASSWORD, auth.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);
    }

    @Override
    public ResponseEntity<String> logout(String refreshToken) {
        String logoutUrl = KEYCLOAK_SERVER_URL + LOGOUT;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = addBody();
        body.add(REFRESH_TOKEN, refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(logoutUrl, request, String.class);

        return ResponseEntity.ok(SESION_CLOSED);
    }

    @Override
    public ResponseEntity<Map> refreshToken(String refreshToken) {
        String tokenUrl = KEYCLOAK_SERVER_URL + TOKEN;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = addBody();
        body.add(GRANT_TYPE, REFRESH_TOKEN);
        body.add(REFRESH_TOKEN, refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);
    }

    private MultiValueMap<String, String> addBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(Constants.CLIENT_ID, CLIENT_ID);
        body.add(Constants.CLIENT_SECRET, CLIENT_SECRET);
        return body;
    }
}
