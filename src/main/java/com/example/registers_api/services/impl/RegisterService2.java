package com.example.registers_api.services.impl;

import com.example.registers_api.models.*;
import com.example.registers_api.repository.AnalitycsRegister;
import com.example.registers_api.repository.RegisterHistoryRepository;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.services.IRegisterService2;
import com.example.registers_api.services.validations.RegistersServiceValidations;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class RegisterService2 implements IRegisterService2 {
    private RegisterRepository registerRepository;
    private RegisterHistoryRepository registerHistoryRepository;
    private AnalitycsRegister analyticsRegisterRepository;
    private RegistersServiceValidations registersServiceValidations;
    @Override
    public void saveRegister(RegisterRequest register, String userEmail) {
        // 1️⃣ Validaciones
        registersServiceValidations.validateResearchLayer(userEmail, register);
        registersServiceValidations.validateRegisterFields(register);
        registersServiceValidations.validateVariablesAndResearchLayer(register);

        // 2️⃣ Agrupar variables en ResearchLayerGroup
        Map<String, ResearchLayerGroup> grouped = new LinkedHashMap<>();
        register.getVariables().forEach(vReq -> {
            String layerId = vReq.getResearchLayerId();
            String layerName = vReq.getResearchLayerName();

            grouped.computeIfAbsent(layerId, id -> ResearchLayerGroup.builder()
                    .researchLayerId(layerId)
                    .researchLayerName(layerName)
                    .variables(new ArrayList<>())
                    .build()
            ).getVariables().add(
                    Variable.builder()
                            .id(vReq.getId())
                            .name(vReq.getVariableName())
                            .type(vReq.getType())
                            .valueAsString(vReq.getValueAsString())
                            .valueAsNumber(vReq.getValueAsNumber())
                            .build()
            );
        });

        RegisterCollection registerCollection = RegisterCollection.builder()
                .registerDate(LocalDateTime.now())
                .updateRegisterDate(LocalDateTime.now())
                .updatedBy(userEmail)
                .patientIdentificationNumber(register.getPatientIdentificationNumber())
                .patientIdentificationType(register.getPatientIdentificationType())
                .registerInfo(new ArrayList<>(grouped.values()))
                .patientBasicInfo(register.getPatient())
                .caregiver(register.getCaregiver())
                .healthProfessional(register.getHealthProfessional())
                .build();

        RegisterCollection saved = registerRepository.save(registerCollection);

        //falta modificar esto y agregarle el caso en donde se agregen los otros de research, caregiver y healthProfesional
        RegistersHistoryCollection history = RegistersHistoryCollection.builder()
                .registerId(saved.getId())
                .updatedBy(userEmail)
                .updatedAt(LocalDateTime.now().toString())
                .operation("CREATE_REGISTER")
                .isResearchLayerGroup(null)
                .isPatientBasicInfo(saved.getPatientBasicInfo())
                .isCaregiverInfo(saved.getCaregiver())
                .isHealthProfessionalInfo(saved.getHealthProfessional())
                .build();
        registerHistoryRepository.save(history);

        // Insertar filas en analytics_register
        List<AnalyticsRegisterCollection> analyticsRows = new ArrayList<>();
        for (ResearchLayerGroup group : saved.getRegisterInfo()) {
            for (Variable var : group.getVariables()) {
                analyticsRows.add(
                        AnalyticsRegisterCollection.builder()
                                .registerId(saved.getId())
                                .readingTimestamp(LocalDateTime.now())
                                .researchLayerName(group.getResearchLayerName())
                                .variableName(var.getName())
                                .variableType(var.getType())
                                .variableValue(
                                        var.getType().equalsIgnoreCase("Numerico")
                                                ? var.getValueAsNumber()
                                                : var.getValueAsString()
                                )
                                .patientIdentificationNumber(saved.getPatientIdentificationNumber())
                                .patientIdentificationType(saved.getPatientIdentificationType())
                                .patientName(saved.getPatientBasicInfo().getName())
                                .patientSex(saved.getPatientBasicInfo().getSex())
                                .patientAgeAtReading(saved.getPatientBasicInfo().getAge())
                                .patientEmail(saved.getPatientBasicInfo().getEmail())
                                .patientPhoneNumber(saved.getPatientBasicInfo().getPhoneNumber())
                                .patientEconomicStatus(saved.getPatientBasicInfo().getEconomicStatus())
                                .patientEducationLevel(saved.getPatientBasicInfo().getEducationLevel())
                                .patientMaritalStatus(saved.getPatientBasicInfo().getMaritalStatus())
                                .patientHometown(saved.getPatientBasicInfo().getHometown())
                                .patientCurrentCity(saved.getPatientBasicInfo().getCurrentCity())
                                .patientFirstCrisisDate(saved.getPatientBasicInfo().getFirstCrisisDate())
                                .patientCrisisStatus(saved.getPatientBasicInfo().getCrisisStatus())
                                .caregiverName(saved.getCaregiver().getName())
                                .caregiverIdentificationNumber(saved.getCaregiver().getIdentificationNumber())
                                .caregiverOccupation(saved.getCaregiver().getOccupation())
                                .healthProfessionalName(saved.getHealthProfessional().getName())
                                .healthProfessionalIdentificationNumber(saved.getHealthProfessional().getIdentificationNumber())
                                .build()
                );
            }
        }
        analyticsRegisterRepository.saveAll(analyticsRows);
    }
}
