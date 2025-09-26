package com.example.registers_api.services.impl;

import com.example.registers_api.dtos.ResearchLayerInfoDTO;
import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.mappers.RegisterMapper;
import com.example.registers_api.mappers.ResearchLayerMapper2;
import com.example.registers_api.mappers.VariablesMapper;
import com.example.registers_api.models.*;
import com.example.registers_api.repository.AnalitycsRegister;
import com.example.registers_api.repository.RegisterHistoryRepository;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.response.*;
import com.example.registers_api.services.AnalyticsPipelineService;
import com.example.registers_api.services.IRegisterService2;
import com.example.registers_api.services.validations.RegistersServiceValidations;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.registers_api.utils.Constants.*;
import static com.example.registers_api.utils.ExceptionConstants.*;

@Component
@AllArgsConstructor
public class RegisterService2 implements IRegisterService2 {
    private RegisterRepository registerRepository;
    private RegisterHistoryRepository registerHistoryRepository;
    private AnalitycsRegister analitycsRegisterRepository;
    private VariableRepository variableRepository;
    private RegistersServiceValidations registersServiceValidations;
    private AnalyticsPipelineService analyticsPipelineService;
    private RegisterMapper registerMapper;

    @Override
    @Transactional
    public void saveRegister(RegisterRequest registerRequest, String userEmail) {

        registersServiceValidations.validateResearchLayer(userEmail, registerRequest.getRegisterInfo().getResearchLayerId());
        registersServiceValidations.validateRegisterFields(registerRequest);
        registersServiceValidations.validateVariablesAndResearchLayer(registerRequest);

        Map<String, VariableCollection> catalog = variableRepository.findAll()
                .stream().collect(Collectors.toMap(VariableCollection::getId, v -> v));

        ResearchLayerMapper2 mapper = new ResearchLayerMapper2();
        ResearchLayerGroup registerInfo = mapper.toResearchLayerGroup(registerRequest.getRegisterInfo(), catalog);

        RegisterCollection registerCollection = RegisterCollection.builder()
                .patientIdentificationNumber(registerRequest.getPatientIdentificationNumber())
                .patientIdentificationType(registerRequest.getPatientIdentificationType())
                .registerInfo(Collections.singletonList(registerInfo))
                .patientBasicInfo(registerRequest.getPatient())
                .caregiver(registerRequest.getCaregiver())
                .build();

        RegisterCollection saved = registerRepository.save(registerCollection);
        saveFirstRegisterInRegisterHistory(saved, userEmail);
        //analyticsPipelineService.insertInitialSnapshot(saved.getId());
    }

    @Override
    @Transactional
    public void updateRegister(String registerId, RegisterRequest registerRequest, String userEmail) {
        RegisterCollection existingRegister = registerRepository.findById(registerId)
                .orElseThrow(() -> new DoesntExistsException(
                        String.format(REGISTER_NOT_FOUND, registerId)
                ));

        registersServiceValidations.validateResearchLayer(userEmail, registerRequest.getRegisterInfo().getResearchLayerId());
        registersServiceValidations.validateRegisterFields(registerRequest);
        registersServiceValidations.validateVariablesAndResearchLayer(registerRequest);

        updateAndSaveHistory(registerRequest, userEmail, existingRegister);

    }

    @Override
    public RegisterResponse2 actualPatientRegisterInfo(Integer patientIdentificationNumber, String researchLayerId) {
        RegisterCollection collection = registerRepository.findByPatientIdentificationNumber(patientIdentificationNumber);
        RegisterResponse2 response = registerMapper.toRegisterResponse(collection);
        List<ResearchLayerGroupResponse> layerInfo = response.getRegisterInfo().stream().filter(layer -> researchLayerId.equals(layer.getResearchLayerId())).collect(Collectors.toList());
        response.setRegisterInfo(layerInfo);
        return response;

    }

    @Override
    public PaginatedResponse getAllRegistersByResearchLayerPaginated(PaginationRequest paginationRequest, String researchLayerId,
                                                                     String userEmail,
                                                                     Integer patientIdentificationNumber) {
        registersServiceValidations.validateResearchLayer(userEmail, researchLayerId);

        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                Sort.by(paginationRequest.getSort(), "changedAt")
        );

        List<String> opcionesLayerGroup = new ArrayList<>(List.of(UPDATE_RESEARCH_LAYER));

        RegistersHistoryCollection firstRegister = registerHistoryRepository.findByPatientIdentificationNumberAndOperation
                (patientIdentificationNumber, REGISTER_CREATED);

        if(getFirstRegisterByResearchLayerId(firstRegister, researchLayerId) != null){
            opcionesLayerGroup.add(0, REGISTER_CREATED);
        }

        Page<RegistersHistoryCollection> page = registerHistoryRepository
                .findResearchLayerHistoryByPatientAndOps(
                        patientIdentificationNumber,
                        opcionesLayerGroup,
                        researchLayerId,
                        pageable);

        List<RegistersHistoryItemResponse> data = page.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new PaginatedResponse<>(
                data,
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );

    }

    @Override
    public PaginatedResponse getAllCaregiverRegistersPaginated(PaginationRequest paginationRequest, Integer patientIdentificationNumber) {
        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                Sort.by(paginationRequest.getSort(), "changedAt")
        );

        List<String> ops = List.of(REGISTER_CREATED, UPDATE_CAREGIVER);

        Page<RegistersHistoryCollection> page = registerHistoryRepository
                .findCaregiverHistoryByPatientAndOps(patientIdentificationNumber, ops, pageable);

        List<RegistersHistoryItemResponse> data = page.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new PaginatedResponse<>(
                data,
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public PaginatedResponse getAllPatientBasicInfoRegistersPaginated(PaginationRequest paginationRequest, Integer patientIdentificationNumber) {

        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                Sort.by(paginationRequest.getSort(), "changedAt")
        );

        List<String> ops = List.of(REGISTER_CREATED, UPDATE_PATIENT_BASIC_INFO);

        Page<RegistersHistoryCollection> page = registerHistoryRepository
                .findPatientHistoryByPatientAndOps(patientIdentificationNumber, ops, pageable);

        List<RegistersHistoryItemResponse> data = page.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new PaginatedResponse<>(
                data,
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );

    }

    @Override
    public PaginatedResponse getAllRegisterInfoByResearchLayerPaginated(
            PaginationRequest paginationRequest, String researchLayerId, String userEmail) {

        registersServiceValidations.validateResearchLayer(userEmail, researchLayerId);

        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                Sort.by(paginationRequest.getSort(), "changedAt")
        );

        List<String> ops = List.of(REGISTER_CREATED, REGISTER_CREATED_SUCCESSFULL, UPDATE_RESEARCH_LAYER);

        Page<RegistersHistoryCollection> page = registerHistoryRepository
                .findResearchLayerHistoryByResearchLayerIdAndOps(
                        researchLayerId,
                        ops,
                        pageable
                );

        List<RegistersHistoryItemResponse> data = page.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new PaginatedResponse<>(
                data,
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public ValidationResponse validateUserAndGetCurrent(String userEmail, String researchLayerId, Integer patientIdentificationNumber) {

        registersServiceValidations.validateResearchLayer(userEmail, researchLayerId);

        Optional<RegisterCollection> opt = registerRepository
                .findFirstByPatientIdentificationNumberOrderByVersionDesc(patientIdentificationNumber);

        if (opt.isEmpty()) {
            return ValidationResponse.builder()
                    .action("patient_doesnt_exist")
                    .build();
        }

        RegisterCollection reg = opt.get();

        ResearchLayerGroup layer = null;
        if (reg.getRegisterInfo() != null) {
            layer = reg.getRegisterInfo()
                    .stream()
                    .filter(r -> researchLayerId.equals(r.getResearchLayerId()))
                    .findFirst()
                    .orElse(null);
        }

        if (layer != null) {
            ResearchLayerInfoDTO layerDto = ResearchLayerInfoDTO.builder()
                    .researchLayerId(layer.getResearchLayerId())
                    .researchLayerName(layer.getResearchLayerName())
                    .variablesInfo(VariablesMapper.toVariablesInfo(layer.getVariables()))
                    .build();

            return ValidationResponse.builder()
                    .action("patient_already_exist_in_layer")
                    .registerId(reg.getId())
                    .patientIdentificationNumber(reg.getPatientIdentificationNumber())
                    .patientIdentificationType(reg.getPatientIdentificationType())
                    .registerInfo(List.of(layerDto))
                    .patientBasicInfo(reg.getPatientBasicInfo())
                    .caregiver(reg.getCaregiver())
                    .build();
        } else {
            return ValidationResponse.builder()
                    .action("patient_doesnt_exist_in_layer")
                    .registerId(reg.getId())
                    .patientIdentificationNumber(reg.getPatientIdentificationNumber())
                    .patientIdentificationType(reg.getPatientIdentificationType())
                    .registerInfo(List.of())
                    .patientBasicInfo(reg.getPatientBasicInfo())
                    .caregiver(reg.getCaregiver())
                    .build();
        }
    }

    @Override
    public void deleteRegister(String registerId) {
        registerRepository.findById(registerId).orElseThrow(() -> new DoesntExistsException(
                String.format(REGISTER_NOT_FOUND, registerId)
        ));

        registerRepository.deleteById(registerId);

        registerHistoryRepository.deleteByRegisterId(registerId);

        //analitycsRegisterRepository.deleteByRegisterId(registerId);

    }

    public void updateAndSaveHistory(RegisterRequest registerRequest,
                                     String userEmail,
                                     RegisterCollection existingRegister) {

        Map<String, VariableCollection> catalog = variableRepository.findAll()
                .stream().collect(Collectors.toMap(VariableCollection::getId, v -> v));

        ResearchLayerMapper2 mapper = new ResearchLayerMapper2();
        ResearchLayerGroup registerInfo = mapper.toResearchLayerGroup(registerRequest.getRegisterInfo(), catalog);

        boolean patientChanged   = !Objects.equals(registerRequest.getPatient(), existingRegister.getPatientBasicInfo());
        boolean caregiverChanged = !Objects.equals(registerRequest.getCaregiver(), existingRegister.getCaregiver());
        boolean needAllLayersSnapshot = false;

        if(patientChanged){
            existingRegister.setPatientBasicInfo(registerRequest.getPatient());
            addInRegisterHistory(existingRegister.getId(),
                    userEmail, UPDATE_PATIENT_BASIC_INFO, registerRequest, registerInfo);
            needAllLayersSnapshot = true;
        }
        if(caregiverChanged){
            existingRegister.setCaregiver(registerRequest.getCaregiver());
            addInRegisterHistory(existingRegister.getId(),
                    userEmail, UPDATE_CAREGIVER, registerRequest, registerInfo);
            needAllLayersSnapshot = true;
        }
        else{
            List<ResearchLayerGroup> newRegisterInfo = updateLayers(existingRegister.getRegisterInfo(),
                    registerInfo);

            existingRegister.setRegisterInfo(newRegisterInfo);
            addInRegisterHistory(existingRegister.getId(),
                    userEmail, UPDATE_RESEARCH_LAYER, registerRequest,registerInfo);
            needAllLayersSnapshot = false;
        }

        registerRepository.save(existingRegister);

//        if (needAllLayersSnapshot) {
//            analyticsPipelineService.insertAllLayersSnapshot(existingRegister.getId());
//        } else {
//            analyticsPipelineService.insertLayerSnapshot(
//                    existingRegister.getId(),
//                    registerRequest.getRegisterInfo().getResearchLayerId()
//            );
//        }
    }

    public List<ResearchLayerGroup> updateLayers(List<ResearchLayerGroup> existingLayers,
                                                 ResearchLayerGroup layerRequest) {
        if (existingLayers == null) {
            existingLayers = new ArrayList<>();
        }

        int index = -1;
        for (int i = 0; i < existingLayers.size(); i++) {
            if (Objects.equals(existingLayers.get(i).getResearchLayerId(), layerRequest.getResearchLayerId())) {
                index = i;
                break;
            }
        }

        if (index >= 0) {
            existingLayers.set(index, layerRequest);
        } else {
            existingLayers.add(layerRequest);
        }

        return existingLayers;
    }

    public void saveFirstRegisterInRegisterHistory(RegisterCollection registerSaved, String userEmail) {

        RegistersHistoryCollection history = RegistersHistoryCollection.builder()
                .registerId(registerSaved.getId())
                .changedBy(userEmail)
                .changedAt(LocalDateTime.now().toString())
                .operation(REGISTER_CREATED)
                .patientIdentificationNumber(registerSaved.getPatientIdentificationNumber())
                .isResearchLayerGroup(registerSaved.getRegisterInfo().get(0))
                .isPatientBasicInfo(registerSaved.getPatientBasicInfo())
                .isCaregiverInfo(registerSaved.getCaregiver())
                .build();

        registerHistoryRepository.save(history);
    }

    public void addInRegisterHistory(String registerId, String userEmail, String operation,
                                                   RegisterRequest registerRequest,
                                                    ResearchLayerGroup researchLayerGroup) {
        RegistersHistoryCollection register = RegistersHistoryCollection.builder()
                .registerId(registerId)
                .changedBy(userEmail)
                .patientIdentificationNumber(registerRequest.getPatientIdentificationNumber())
                .operation(operation)
                .changedAt(LocalDateTime.now().toString())
                .build();

        switch (operation) {
            case UPDATE_RESEARCH_LAYER:
                register.setIsResearchLayerGroup(researchLayerGroup);
                break;

            case UPDATE_PATIENT_BASIC_INFO:
                register.setIsPatientBasicInfo(registerRequest.getPatient());
                break;

            case UPDATE_CAREGIVER:
                register.setIsCaregiverInfo(registerRequest.getCaregiver());
                break;

            default:
                throw new IllegalArgumentException(TYPE_UNDEFINED);
        }
        registerHistoryRepository.save(register);
    }

    public ResearchLayerGroup getFirstRegisterByResearchLayerId(RegistersHistoryCollection firstRegister, String researchLayerId) {

        if(firstRegister != null && firstRegister.getIsResearchLayerGroup().getResearchLayerId().equals(researchLayerId)){
            return firstRegister.getIsResearchLayerGroup();
        }
        else{
            return null;
        }
    }

    private RegistersHistoryItemResponse toResponse(RegistersHistoryCollection src) {
        RegistersHistoryItemResponse r = new RegistersHistoryItemResponse();
        r.setId(src.getId());
        r.setRegisterId(src.getRegisterId());
        r.setChangedBy(src.getChangedBy());
        r.setChangedAt(src.getChangedAt());
        r.setOperation(src.getOperation());
        r.setPatientIdentificationNumber(src.getPatientIdentificationNumber());
        r.setIsResearchLayerGroup(src.getIsResearchLayerGroup());
        r.setIsPatientBasicInfo(src.getIsPatientBasicInfo());
        r.setIsCaregiverInfo(src.getIsCaregiverInfo());

        return r;
    }

}
