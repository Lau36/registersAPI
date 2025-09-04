package com.example.registers_api.services.impl;

import com.example.registers_api.models.TermsAndConditionsCollection;
import com.example.registers_api.repository.TermsConditionsRepository;
import com.example.registers_api.response.TermsConditionsResponse;
import com.example.registers_api.services.ITermsAndConditions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TermsAndConditionsService implements ITermsAndConditions {
    TermsConditionsRepository termsConditionsRepository;

    @Override
    public TermsConditionsResponse getTermsAndConditions() {
        TermsAndConditionsCollection collection = termsConditionsRepository.findAll().get(0);
        TermsConditionsResponse termsConditionsResponse = new TermsConditionsResponse();
        termsConditionsResponse.setTermsAndConditionsInfo(collection.getTermsConditions());
        return termsConditionsResponse;
    }
}
