package com.example.registers_api.response;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class TermsConditionsResponse {
    private String termsAndConditionsInfo;
}

