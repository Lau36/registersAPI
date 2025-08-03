package com.example.registers_api.services;

import com.example.registers_api.models.*;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.request.SortDirection;
import com.example.registers_api.response.PaginatedResponse;
import com.example.registers_api.services.impl.RegisterService;
import com.example.registers_api.services.validations.RegistersServiceValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {
    @Mock
    private RegisterRepository registerRepository;

    @Mock
    private VariableRepository variableRepository;

    @Mock
    private ResearchLayerRepository researchLayerRepository;

    @Mock
    private RegistersServiceValidations registersServiceValidations;

    @InjectMocks
    private RegisterService registerService;

    @Mock
    private RegisterRequest registerRequest;

    @Mock
    private PaginationRequest paginationRequest;

    @Mock
    private RegisterCollection registerCollection;

    private Patient patient;
    private Variable variable;
    private HealthProfessional healthProfessional;
    private Caregiver caregiver;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.toBuilder()
                .name("Juan")
                .sex("M")
                .birthDate(LocalDate.of(1990, 1, 1))
                .age(33)
                .email("juan@mail.com")
                .phoneNumber("1234567890")
                .economicStatus("Medio")
                .educationLevel("Universitario")
                .maritalStatus("Soltero")
                .hometown("CiudadA")
                .currentCity("CiudadB")
                .firstCrisisDate("2020-01-01")
                .crisisStatus("Activo")
                .build();

        variable = new Variable();
        variable.toBuilder()
                .id("1")
                .value("value")
                .type("type")
                .researchLayerId("researchLayerId")
                .variableName("name")
                .researchLayerName("name2")
                .build();

        healthProfessional = new HealthProfessional();
        healthProfessional.toBuilder()
                .id("2")
                .identificationNumber(1109887612)
                .name("Juan")
                .build();
        caregiver = new Caregiver();
        caregiver.toBuilder()
                .name("Maria")
                .age(50)
                .identificationType("CC")
                .identificationNumber(111)
                .educationLevel("Bachiller")
                .occupation("Ama de casa")
                .build();


    }

//    @Test
//    void testSaveRegister() {
//
//        String userEmail = "juan@mail.com";
//
//        List<Variable> variables = List.of(variable);
//        VariableCollection variableFromDb = VariableCollection.builder()
//                .id(variable.getId())
//                .name(variable.getVariableName())
//                .build();
//
//        ResearchLayerCollection researchLayerFromDb = new ResearchLayerCollection()
//
//        researchLayerFromDb.toBuilder()
//                .id(variable.getResearchLayerId())
//                .name(variable.getResearchLayerName())
//                .build();
//
//        when(registerRequest.getVariables()).thenReturn(variables);
//        when(registerRequest.getPatient()).thenReturn(patient);
//        when(registerRequest.getCaregiver()).thenReturn(caregiver);
//        when(registerRequest.getHealthProfessional()).thenReturn(healthProfessional);
//        when(registerRequest.getPatientIdentificationNumber()).thenReturn(123);
//        when(registerRequest.getPatientIdentificationType()).thenReturn("CC");
//
//        when(variableRepository.findById(variable.getId())).thenReturn(Optional.of(variableFromDb));
//        when(researchLayerRepository.findById(variable.getResearchLayerId())).thenReturn(Optional.of(researchLayerFromDb));
//
//
//        doNothing().when(registersServiceValidations).validateResearchLayer(userEmail, registerRequest);
//        doNothing().when(registersServiceValidations).validateRegisterFields(registerRequest);
//        doNothing().when(registersServiceValidations).validateVariablesAndResearchLayer(registerRequest);
//
//        registerService.saveRegister(registerRequest, userEmail);
//
//        verify(registersServiceValidations).validateResearchLayer(userEmail, registerRequest);
//        verify(registersServiceValidations).validateRegisterFields(registerRequest);
//        verify(registersServiceValidations).validateVariablesAndResearchLayer(registerRequest);
//
//        RegisterCollection expectedRegisterCollection = RegisterCollection.builder()
//                .registerDate(LocalDateTime.now())
//                .patientIdentificationNumber(123)
//                .patientIdentificationType("CC")
//                .variables(variables)
//                .patientBasicInfo(patient)
//                .caregiver(caregiver)
//                .healthProfessional(healthProfessional)
//                .build();
//
//        verify(registerRepository).save(argThat(registerCollection ->
//                registerCollection.getPatientIdentificationNumber().equals(expectedRegisterCollection.getPatientIdentificationNumber()) &&
//                        registerCollection.getPatientIdentificationType().equals(expectedRegisterCollection.getPatientIdentificationType())
//        ));
//    }

    @Test
    void testGetAllRegistersPaginated() {
        when(paginationRequest.getPage()).thenReturn(0);
        when(paginationRequest.getSize()).thenReturn(10);
        when(paginationRequest.getSort()).thenReturn("registerDate");
        when(paginationRequest.getSortDirection()).thenReturn(SortDirection.ASC);

        List<RegisterCollection> registersList = List.of(registerCollection);
        when(registerRepository.findAllBy(any(PageRequest.class))).thenReturn(registersList);
        when(registerRepository.count()).thenReturn(1L);

        PaginatedResponse response = registerService.getAllRegistersPaginated(paginationRequest);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        verify(registerRepository).findAllBy(any(PageRequest.class));
    }

    @Test
    void testUpdateRegister() {
        String registerId = "reg123";
        String userEmail = "juan@mail.com";
        RegisterCollection existingRegister = mock(RegisterCollection.class);

        when(registerRepository.findById(registerId)).thenReturn(Optional.of(existingRegister));
        when(registerRequest.getVariables()).thenReturn(List.of(new Variable("v1","String", "String", "value", "String", "rl1")));
        when(registerRequest.getPatient()).thenReturn(patient);

        doNothing().when(registersServiceValidations).validateResearchLayer(userEmail, registerRequest);
        doNothing().when(registersServiceValidations).validateRegisterFields(registerRequest);
        doNothing().when(registersServiceValidations).validateVariablesAndResearchLayer(registerRequest);

        registerService.updateRegister(registerId, userEmail, registerRequest);

        verify(registerRepository).save(existingRegister);
        verify(registersServiceValidations).validateResearchLayer(userEmail, registerRequest);
        verify(registersServiceValidations).validateRegisterFields(registerRequest);
        verify(registersServiceValidations).validateVariablesAndResearchLayer(registerRequest);
    }

    @Test
    void testGetAllRegistersByPatientPaginated() {
        Integer patientIdentificationNumber = 123;
        when(paginationRequest.getPage()).thenReturn(0);
        when(paginationRequest.getSize()).thenReturn(10);
        when(paginationRequest.getSort()).thenReturn("registerDate");
        when(paginationRequest.getSortDirection()).thenReturn(SortDirection.ASC);

        List<RegisterCollection> registersList = List.of(registerCollection);

        when(registerRepository.findAllByPatientIdentificationNumber(eq(patientIdentificationNumber), any(PageRequest.class)))
                .thenReturn(registersList);
        when(registerRepository.countByPatientIdentificationNumber(patientIdentificationNumber)).thenReturn(1);

        PaginatedResponse response = registerService.getAllRegistersByPatientPaginated(paginationRequest, patientIdentificationNumber);

        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());

        verify(registerRepository).findAllByPatientIdentificationNumber(eq(patientIdentificationNumber), any(PageRequest.class));
    }


    @Test
    void testGetAllRegistersByHealthProfesionalPaginated() {
        Integer healthProfesionalIdentificationNumber = 1109660245;
        when(paginationRequest.getPage()).thenReturn(0);
        when(paginationRequest.getSize()).thenReturn(10);
        when(paginationRequest.getSort()).thenReturn("registerDate");
        when(paginationRequest.getSortDirection()).thenReturn(SortDirection.ASC);

        List<RegisterCollection> registersList = List.of(registerCollection);

        when(registerRepository.findAllByHealthProfessionalIdentificationNumber(eq(healthProfesionalIdentificationNumber), any(PageRequest.class)))
                .thenReturn(registersList);
        when(registerRepository.countByHealthProfessionalIdentificationNumber(healthProfesionalIdentificationNumber)).thenReturn(1);

        PaginatedResponse response = registerService.getAllRegistersByHealthProfesionalPaginated(paginationRequest, healthProfesionalIdentificationNumber);

        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());

        verify(registerRepository).findAllByHealthProfessionalIdentificationNumber(eq(healthProfesionalIdentificationNumber), any(PageRequest.class));
    }


    @Test
    void testGetAllRegistersByResearchLayerPaginated() {
        String researchLayerId = "rl1";
        when(paginationRequest.getPage()).thenReturn(0);
        when(paginationRequest.getSize()).thenReturn(10);
        when(paginationRequest.getSort()).thenReturn("registerDate");
        when(paginationRequest.getSortDirection()).thenReturn(SortDirection.ASC);

        List<RegisterCollection> registersList = List.of(registerCollection);

        when(registerRepository.findAllByVariablesResearchLayerId(eq(researchLayerId), any(PageRequest.class)))
                .thenReturn(registersList);
        when(registerRepository.countByVariablesResearchLayerId(researchLayerId)).thenReturn(1);

        PaginatedResponse response = registerService.getAllRegistersByResearchLayerPaginated(paginationRequest, researchLayerId);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());

        verify(registerRepository).findAllByVariablesResearchLayerId(eq(researchLayerId), any(PageRequest.class));
    }


    @Test
    void testDeleteRegister() {
        String registerId = "reg123";
        when(registerRepository.findById(registerId)).thenReturn(Optional.of(new RegisterCollection()));

        registerService.deleteRegister(registerId);

        verify(registerRepository, times(1)).deleteById(registerId);
    }

}
