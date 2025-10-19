package com.example.registers_api.services;

import com.example.registers_api.dtos.ResearchLayerInfoDTO;
import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.mappers.RegisterMapper;
import com.example.registers_api.models.*;
import com.example.registers_api.repository.AnalitycsRegister;
import com.example.registers_api.repository.RegisterHistoryRepository;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.request.ResearchLayerGroupRequest;
import com.example.registers_api.response.PaginatedResponse;
import com.example.registers_api.response.RegisterResponse2;
import com.example.registers_api.response.ResearchLayerGroupResponse;
import com.example.registers_api.response.ValidationResponse;
import com.example.registers_api.services.impl.RegisterService2;
import com.example.registers_api.services.validations.RegistersServiceValidations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.registers_api.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterService2Test {

    @Mock
    RegisterRepository registerRepository;
    @Mock
    RegisterHistoryRepository registerHistoryRepository;
    @Mock
    AnalitycsRegister analitycsRegisterRepository;
    @Mock
    VariableRepository variableRepository;
    @Mock
    RegistersServiceValidations registersServiceValidations;
    @Mock
    com.example.registers_api.services.AnalyticsPipelineService analyticsPipelineService;
    @Mock
    RegisterMapper registerMapper;

    @InjectMocks
    RegisterService2 service;

    final String userEmail = "tester@acme.com";
    final String researchLayerId = "RL-001";
    final Variable variable = new Variable("id", "name", "type", "valueString", null);
    final ResearchLayerGroup researchLayerGroup = new ResearchLayerGroup("researchId",
            "nombre capa",
            List.of(variable)
            );

    @BeforeEach
    void init() {
        // Nada especial por ahora
    }

    @Test
    @DisplayName("saveRegister: guarda registro, historia inicial y snapshot inicial")
    void saveRegister_ok() {
        // Arrange
        RegisterRequest req = buildRegisterRequest(123, researchLayerId);
        when(variableRepository.findAll()).thenReturn(List.of(
                VariableCollection.builder().id("V1").build()
        ));

        RegisterCollection saved = RegisterCollection.builder()
                .id("REG-1")
                .patientIdentificationNumber(req.getPatientIdentificationNumber())
                .patientIdentificationType(req.getPatientIdentificationType())
                .registerInfo(List.of(researchLayerGroup))
                .patientBasicInfo(req.getPatient())
                .caregiver(req.getCaregiver())
                .build();

        when(registerRepository.save(any(RegisterCollection.class))).thenReturn(saved);

        // Act
        service.saveRegister(req, userEmail);

        // Assert
        verify(registersServiceValidations).validateResearchLayer(eq(userEmail), eq(researchLayerId));
        verify(registersServiceValidations).validateRegisterFields(eq(req));
        verify(registersServiceValidations).validateVariablesAndResearchLayer(eq(req));

        verify(registerRepository).save(any(RegisterCollection.class));
        verify(registerHistoryRepository).save(argThat(h ->
                h.getRegisterId().equals("REG-1")
                        && REGISTER_CREATED.equals(h.getOperation())
                        && h.getIsPatientBasicInfo() != null
                        && h.getIsCaregiverInfo() != null
                        && h.getIsResearchLayerGroup() != null
        ));
        verify(analyticsPipelineService).insertInitialSnapshot("REG-1");
    }

    @Test
    @DisplayName("updateRegister: cuando existe, valida y delega a updateAndSaveHistory")
    void updateRegister_ok() {
        // Arrange
        RegisterRequest req = buildRegisterRequest(123, researchLayerId);

        RegisterCollection existing = RegisterCollection.builder()
                .id("REG-2")
                .patientIdentificationNumber(123)
                .patientIdentificationType("CC")
                .registerInfo(List.of(researchLayerGroup))
                .patientBasicInfo(Patient.builder().name("Old").build())
                .caregiver(Caregiver.builder().name("OldC").build())
                .build();

        when(registerRepository.findById("REG-2")).thenReturn(Optional.of(existing));

        doNothing().when(service).updateAndSaveHistory(any(), anyString(), any());

        // Act
        service.updateRegister("REG-2", req, userEmail);

        // Assert
        verify(registersServiceValidations).validateResearchLayer(userEmail, researchLayerId);
        verify(registersServiceValidations).validateRegisterFields(req);
        verify(registersServiceValidations).validateVariablesAndResearchLayer(req);
        verify(service).updateAndSaveHistory(eq(req), eq(userEmail), eq(existing));
    }

    @Test
    @DisplayName("updateRegister: lanza DoesntExistsException si no existe")
    void updateRegister_notFound() {
        when(registerRepository.findById("X")).thenReturn(Optional.empty());
        RegisterRequest req = buildRegisterRequest(123, researchLayerId);
        assertThrows(DoesntExistsException.class, () -> service.updateRegister("X", req, userEmail));
    }

    @Test
    @DisplayName("actualPatientRegisterInfo: mapea y filtra por researchLayerId")
    void actualPatientRegisterInfo_filtersLayer() {
        RegisterCollection found = RegisterCollection.builder().id("RID").build();

        RegisterResponse2 mapped = new RegisterResponse2();
        mapped.setRegisterInfo(List.of(
                ResearchLayerGroupResponse.builder().researchLayerId("RL-001").build(),
                ResearchLayerGroupResponse.builder().researchLayerId("RL-OTHER").build()
        ));

        when(registerRepository.findByPatientIdentificationNumber(123)).thenReturn(found);
        when(registerMapper.toRegisterResponse(found)).thenReturn(mapped);

        RegisterResponse2 resp = service.actualPatientRegisterInfo(123, "RL-001");
        assertEquals(1, resp.getRegisterInfo().size());
        assertEquals("RL-001", resp.getRegisterInfo().get(0).getResearchLayerId());
    }

    @Test
    @DisplayName("getAllRegistersByResearchLayerPaginated: incluye REGISTER_CREATED si el primer registro coincide con la capa")
    void getAllRegistersByResearchLayerPaginated_includesCreatedOp() {
        PaginationRequest pr = new PaginationRequest();
        pr.setPage(0);
        pr.setSize(10);
        pr.setSort(String.valueOf(Sort.Direction.DESC));

        RegistersHistoryCollection first = RegistersHistoryCollection.builder()
                .isResearchLayerGroup(ResearchLayerGroup.builder().researchLayerId("RL-001").build())
                .build();

        doNothing().when(registersServiceValidations)
                .validateResearchLayer(eq(userEmail), eq("RL-001"));
        when(registerHistoryRepository.findByPatientIdentificationNumberAndOperation(123, REGISTER_CREATED))
                .thenReturn(first);

        List<RegistersHistoryCollection> content = List.of(
                RegistersHistoryCollection.builder()
                        .id("H1").registerId("R1").operation(REGISTER_CREATED).changedAt(LocalDateTime.now().toString())
                        .patientIdentificationNumber(123).build()
        );
        Page<RegistersHistoryCollection> page = new PageImpl<>(content, PageRequest.of(0,10), 1);

        when(registerHistoryRepository.findResearchLayerHistoryByPatientAndOps(
                eq(123),
                argThat(ops -> ops.contains(REGISTER_CREATED) && ops.contains(UPDATE_RESEARCH_LAYER)),
                eq("RL-001"),
                any(Pageable.class))
        ).thenReturn(page);

        PaginatedResponse resp = service.getAllRegistersByResearchLayerPaginated(pr, "RL-001", userEmail, 123);
        assertEquals(1, resp.getTotalElements());
        assertEquals(1, ((List<?>)resp.getData()).size());
    }

    // -----------------------------------------
    // getAllCaregiverRegistersPaginated
    // -----------------------------------------
    @Test
    @DisplayName("getAllCaregiverRegistersPaginated: pagina y mapea")
    void getAllCaregiverRegistersPaginated_ok() {
        PaginationRequest pr = pageReq();
        List<RegistersHistoryCollection> content = List.of(
                RegistersHistoryCollection.builder().id("H1").operation(REGISTER_CREATED).build(),
                RegistersHistoryCollection.builder().id("H2").operation(UPDATE_CAREGIVER).build()
        );
        when(registerHistoryRepository.findCaregiverHistoryByPatientAndOps(eq(123), anyList(), any()))
                .thenReturn(new PageImpl<>(content, PageRequest.of(0,10), 2));

        PaginatedResponse resp = service.getAllCaregiverRegistersPaginated(pr, 123);
        assertEquals(2, resp.getTotalElements());
    }

    // -----------------------------------------
    // getAllPatientBasicInfoRegistersPaginated
    // -----------------------------------------
    @Test
    @DisplayName("getAllPatientBasicInfoRegistersPaginated: pagina y mapea")
    void getAllPatientBasicInfoRegistersPaginated_ok() {
        PaginationRequest pr = pageReq();
        List<RegistersHistoryCollection> content = List.of(
                RegistersHistoryCollection.builder().id("H1").operation(REGISTER_CREATED).build(),
                RegistersHistoryCollection.builder().id("H2").operation(UPDATE_PATIENT_BASIC_INFO).build()
        );
        when(registerHistoryRepository.findPatientHistoryByPatientAndOps(eq(123), anyList(), any()))
                .thenReturn(new PageImpl<>(content, PageRequest.of(0,10), 2));

        PaginatedResponse resp = service.getAllPatientBasicInfoRegistersPaginated(pr, 123);
        assertEquals(2, resp.getTotalElements());
    }

    // -----------------------------------------
    // getAllRegisterInfoByResearchLayerPaginated
    // -----------------------------------------
    @Test
    @DisplayName("getAllRegisterInfoByResearchLayerPaginated: valida y pagina")
    void getAllRegisterInfoByResearchLayerPaginated_ok() {
        PaginationRequest pr = pageReq();
        doNothing().when(registersServiceValidations)
                .validateResearchLayer(eq(userEmail), eq(researchLayerId));

        List<RegistersHistoryCollection> content = List.of(
                RegistersHistoryCollection.builder().id("H1").operation(REGISTER_CREATED).build(),
                RegistersHistoryCollection.builder().id("H2").operation(REGISTER_CREATED_SUCCESSFULL).build(),
                RegistersHistoryCollection.builder().id("H3").operation(UPDATE_RESEARCH_LAYER).build()
        );
        when(registerHistoryRepository.findResearchLayerHistoryByResearchLayerIdAndOps(eq(researchLayerId), anyList(), any()))
                .thenReturn(new PageImpl<>(content, PageRequest.of(0,10), 3));

        PaginatedResponse resp = service.getAllRegisterInfoByResearchLayerPaginated(pr, researchLayerId, userEmail);
        assertEquals(3, resp.getTotalElements());
    }

    // -----------------------------------------
    // validateUserAndGetCurrent
    // -----------------------------------------
    @Nested
    class ValidateUserAndGetCurrentTests {
        @Test
        @DisplayName("validateUserAndGetCurrent: si no hay registro -> action=patient_doesnt_exist")
        void validateUserAndGetCurrent_noRegister() {
            doNothing().when(registersServiceValidations)
                    .validateResearchLayer(eq(userEmail), eq(researchLayerId));
            when(registerRepository.findFirstByPatientIdentificationNumberOrderByVersionDesc(123))
                    .thenReturn(Optional.empty());

            ValidationResponse vr = service.validateUserAndGetCurrent(userEmail, researchLayerId, 123);
            assertEquals("patient_doesnt_exist", vr.getAction());
        }

        @Test
        @DisplayName("validateUserAndGetCurrent: si existe y tiene la capa -> action=patient_already_exist_in_layer")
        void validateUserAndGetCurrent_existsWithLayer() {
            doNothing().when(registersServiceValidations)
                    .validateResearchLayer(eq(userEmail), eq(researchLayerId));

            RegisterCollection reg = RegisterCollection.builder()
                    .id("RID")
                    .patientIdentificationNumber(123)
                    .patientIdentificationType("CC")
                    .patientBasicInfo(Patient.builder().name("P").build())
                    .caregiver(Caregiver.builder().name("C").build())
                    .registerInfo(List.of(
                            ResearchLayerGroup.builder().researchLayerId(researchLayerId).researchLayerName("Layer").variables(List.of()).build()
                    ))
                    .build();

            when(registerRepository.findFirstByPatientIdentificationNumberOrderByVersionDesc(123))
                    .thenReturn(Optional.of(reg));

            ValidationResponse vr = service.validateUserAndGetCurrent(userEmail, researchLayerId, 123);
            assertEquals("patient_already_exist_in_layer", vr.getAction());
            assertEquals("RID", vr.getRegisterId());
            assertEquals(1, vr.getRegisterInfo().size());
            assertEquals(researchLayerId, vr.getRegisterInfo().get(0).getResearchLayerId());
        }

        @Test
        @DisplayName("validateUserAndGetCurrent: si existe y NO tiene la capa -> action=patient_doesnt_exist_in_layer")
        void validateUserAndGetCurrent_existsWithoutLayer() {
            doNothing().when(registersServiceValidations)
                    .validateResearchLayer(eq(userEmail), eq(researchLayerId));

            RegisterCollection reg = RegisterCollection.builder()
                    .id("RID")
                    .patientIdentificationNumber(123)
                    .patientIdentificationType("CC")
                    .patientBasicInfo(Patient.builder().name("P").build())
                    .caregiver(Caregiver.builder().name("C").build())
                    .registerInfo(List.of(
                            ResearchLayerGroup.builder().researchLayerId("OTHER").build()
                    ))
                    .build();

            when(registerRepository.findFirstByPatientIdentificationNumberOrderByVersionDesc(123))
                    .thenReturn(Optional.of(reg));

            ValidationResponse vr = service.validateUserAndGetCurrent(userEmail, researchLayerId, 123);
            assertEquals("patient_doesnt_exist_in_layer", vr.getAction());
            assertEquals("RID", vr.getRegisterId());
            assertNotNull(vr.getPatientBasicInfo());
            assertNotNull(vr.getCaregiver());
            assertTrue(vr.getRegisterInfo().isEmpty());
        }
    }

    @Test
    @DisplayName("deleteRegister: elimina registro e historia")
    void deleteRegister_ok() {
        when(registerRepository.findById("RID")).thenReturn(Optional.of(RegisterCollection.builder().id("RID").build()));

        service.deleteRegister("RID");

        verify(registerRepository).deleteById("RID");
        verify(registerHistoryRepository).deleteByRegisterId("RID");
        // verify(analitycsRegisterRepository, never()).deleteByRegisterId(any()); // estaba comentado en el servicio
    }

    @Test
    @DisplayName("deleteRegister: lanza DoesntExistsException si no existe")
    void deleteRegister_notFound() {
        when(registerRepository.findById("RID")).thenReturn(Optional.empty());
        assertThrows(DoesntExistsException.class, () -> service.deleteRegister("RID"));
    }

    @Test
    @DisplayName("updateAndSaveHistory: patientChanged=true => UPDATE_PATIENT_BASIC_INFO y snapshot de todas las capas")
    void updateAndSaveHistory_patientChanged() {
        RegisterCollection existing = RegisterCollection.builder()
                .id("RID")
                .patientBasicInfo(Patient.builder().name("Old").build())
                .caregiver(Caregiver.builder().name("Same").build())
                .registerInfo(List.of(
                        ResearchLayerGroup.builder().researchLayerId(researchLayerId).build()
                ))
                .build();

        RegisterRequest req = buildRegisterRequest(123, researchLayerId);
        req.setPatient(Patient.builder().name("New").build()); // cambia patient
        req.setCaregiver(Caregiver.builder().name("Same").build()); // no cambia caregiver

        when(variableRepository.findAll()).thenReturn(List.of());
        when(registerRepository.save(any())).thenReturn(existing);

        service.updateAndSaveHistory(req, userEmail, existing);

        verify(registerHistoryRepository).save(argThat(h -> UPDATE_PATIENT_BASIC_INFO.equals(h.getOperation())));
        verify(registerRepository).save(existing);
        verify(analyticsPipelineService).insertAllLayersSnapshot("RID");
        verify(analyticsPipelineService, never()).insertLayerSnapshot(any(), any());
    }

    @Test
    @DisplayName("updateAndSaveHistory: caregiverChanged=true => UPDATE_CAREGIVER y snapshot de todas las capas")
    void updateAndSaveHistory_caregiverChanged() {
        RegisterCollection existing = RegisterCollection.builder()
                .id("RID")
                .patientBasicInfo(Patient.builder().name("Same").build())
                .caregiver(Caregiver.builder().name("Old").build())
                .registerInfo(List.of())
                .build();

        RegisterRequest req = buildRegisterRequest(123, researchLayerId);
        req.setPatient(Patient.builder().name("Same").build());
        req.setCaregiver(Caregiver.builder().name("New").build()); // cambia caregiver

        when(variableRepository.findAll()).thenReturn(List.of());
        when(registerRepository.save(any())).thenReturn(existing);

        service.updateAndSaveHistory(req, userEmail, existing);

        verify(registerHistoryRepository).save(argThat(h -> UPDATE_CAREGIVER.equals(h.getOperation())));
        verify(analyticsPipelineService).insertAllLayersSnapshot("RID");
    }

    @Test
    @DisplayName("updateAndSaveHistory: sin cambios en patient/caregiver => UPDATE_RESEARCH_LAYER y snapshot solo de la capa")
    void updateAndSaveHistory_layerOnly() {
        RegisterCollection existing = RegisterCollection.builder()
                .id("RID")
                .patientBasicInfo(Patient.builder().name("Same").build())
                .caregiver(Caregiver.builder().name("Same").build())
                .registerInfo(List.of(researchLayerGroup))
                .build();

        RegisterRequest req = buildRegisterRequest(123, researchLayerId);

        when(variableRepository.findAll()).thenReturn(List.of());
        when(registerRepository.save(any())).thenReturn(existing);

        service.updateAndSaveHistory(req, userEmail, existing);

        verify(registerHistoryRepository).save(argThat(h -> UPDATE_RESEARCH_LAYER.equals(h.getOperation())));
        verify(analyticsPipelineService).insertLayerSnapshot("RID", researchLayerId);
        verify(analyticsPipelineService, never()).insertAllLayersSnapshot(any());
    }

    @Test
    @DisplayName("updateLayers: reemplaza si existe y agrega si no existe")
    void updateLayers_replaceOrAppend() {
        List<ResearchLayerGroup> existing = new ArrayList<>();
        existing.add(ResearchLayerGroup.builder().researchLayerId("A").researchLayerName("OldA").build());

        // Reemplaza
        ResearchLayerGroup reqA = ResearchLayerGroup.builder().researchLayerId("A").researchLayerName("NewA").build();
        List<ResearchLayerGroup> afterReplace = service.updateLayers(existing, reqA);
        assertEquals(1, afterReplace.size());
        assertEquals("NewA", afterReplace.get(0).getResearchLayerName());

        // Agrega
        ResearchLayerGroup reqB = ResearchLayerGroup.builder().researchLayerId("B").researchLayerName("NewB").build();
        List<ResearchLayerGroup> afterAppend = service.updateLayers(afterReplace, reqB);
        assertEquals(2, afterAppend.size());
        assertTrue(afterAppend.stream().anyMatch(l -> "B".equals(l.getResearchLayerId())));
    }

    // -----------------------------------------
    // saveFirstRegisterInRegisterHistory
    // -----------------------------------------
    @Test
    @DisplayName("saveFirstRegisterInRegisterHistory: guarda evento REGISTER_CREATED con snapshots iniciales")
    void saveFirstRegisterInRegisterHistory_ok() {
        RegisterCollection saved = RegisterCollection.builder()
                .id("RID")
                .patientIdentificationNumber(123)
                .registerInfo(List.of(ResearchLayerGroup.builder().researchLayerId("RL-001").build()))
                .patientBasicInfo(Patient.builder().name("P").build())
                .caregiver(Caregiver.builder().name("C").build())
                .build();

        service.saveFirstRegisterInRegisterHistory(saved, userEmail);

        verify(registerHistoryRepository).save(argThat(h ->
                "RID".equals(h.getRegisterId())
                        && REGISTER_CREATED.equals(h.getOperation())
                        && h.getIsResearchLayerGroup() != null
                        && h.getIsPatientBasicInfo() != null
                        && h.getIsCaregiverInfo() != null
                        && userEmail.equals(h.getChangedBy())
        ));
    }

    // -----------------------------------------
    // addInRegisterHistory
    // -----------------------------------------
    @Test
    @DisplayName("addInRegisterHistory: setea isResearchLayerGroup en UPDATE_RESEARCH_LAYER")
    void addInRegisterHistory_updateLayer() {
        RegisterRequest req = buildRegisterRequest(123, researchLayerId);
        service.addInRegisterHistory("RID", userEmail, UPDATE_RESEARCH_LAYER,
                req, ResearchLayerGroup.builder().researchLayerId(researchLayerId).build());

        verify(registerHistoryRepository).save(argThat(h ->
                UPDATE_RESEARCH_LAYER.equals(h.getOperation()) && h.getIsResearchLayerGroup() != null
        ));
    }

    @Test
    @DisplayName("addInRegisterHistory: setea isPatientBasicInfo en UPDATE_PATIENT_BASIC_INFO")
    void addInRegisterHistory_updatePatient() {
        RegisterRequest req = buildRegisterRequest(123, researchLayerId);
        service.addInRegisterHistory("RID", userEmail, UPDATE_PATIENT_BASIC_INFO,
                req, null);

        verify(registerHistoryRepository).save(argThat(h ->
                UPDATE_PATIENT_BASIC_INFO.equals(h.getOperation()) && h.getIsPatientBasicInfo() != null
        ));
    }

    @Test
    @DisplayName("addInRegisterHistory: setea isCaregiverInfo en UPDATE_CAREGIVER")
    void addInRegisterHistory_updateCaregiver() {
        RegisterRequest req = buildRegisterRequest(123, researchLayerId);
        service.addInRegisterHistory("RID", userEmail, UPDATE_CAREGIVER,
                req, null);

        verify(registerHistoryRepository).save(argThat(h ->
                UPDATE_CAREGIVER.equals(h.getOperation()) && h.getIsCaregiverInfo() != null
        ));
    }

    @Test
    @DisplayName("addInRegisterHistory: lanza IllegalArgumentException si operation desconocida")
    void addInRegisterHistory_unknown() {
        RegisterRequest req = buildRegisterRequest(123, researchLayerId);
        assertThrows(IllegalArgumentException.class, () ->
                service.addInRegisterHistory("RID", userEmail, "UNKNOWN", req, null)
        );
    }

    @Test
    @DisplayName("getFirstRegisterByResearchLayerId: devuelve la capa si coincide, si no null")
    void getFirstRegisterByResearchLayerId_ok() {
        RegistersHistoryCollection first = RegistersHistoryCollection.builder()
                .isResearchLayerGroup(ResearchLayerGroup.builder().researchLayerId("RL-001").build())
                .build();

        assertNotNull(service.getFirstRegisterByResearchLayerId(first, "RL-001"));
        assertNull(service.getFirstRegisterByResearchLayerId(first, "OTHER"));
        assertNull(service.getFirstRegisterByResearchLayerId(null, "RL-001"));
    }

    private PaginationRequest pageReq() {
        PaginationRequest pr = new PaginationRequest();
        pr.setPage(0);
        pr.setSize(10);
        pr.setSort(String.valueOf(Sort.Direction.DESC));
        return pr;
    }

    private RegisterRequest buildRegisterRequest(int pin, String layerId) {
        ResearchLayerGroupRequest reqInfo = ResearchLayerGroupRequest.builder()
                .researchLayerId(layerId)
                .variablesInfo(List.of())
                .build();

        RegisterRequest req = new RegisterRequest();
        req.setPatientIdentificationNumber(pin);
        req.setPatientIdentificationType("CC");
        req.setRegisterInfo(reqInfo);
        req.setPatient(Patient.builder().name("John").build());
        req.setCaregiver(Caregiver.builder().name("Jane").build());
        return req;
    }
}

