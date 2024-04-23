package com.helios.api.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.helios.api.dto.PatientDto;
import com.helios.api.dto.PatientResponseDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.dto.UserDto;
import com.helios.api.entity.Patient;
import com.helios.api.entity.Role;
import com.helios.api.entity.User;
import com.helios.api.repository.PatientRepository;
import com.helios.api.repository.RoleRepository;
import com.helios.api.service.PatientService;
import com.helios.api.service.UserService;
import com.helios.api.util.ResponseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class PatientServiceImplTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePatient() {
        // Mock input data
        PatientDto patientDto = new PatientDto();
        patientDto.setEmail("test@example.com");
        patientDto.setNic("199620200917");
        UserDto userDto = new UserDto("testuser", "Test User", "test@example.com", "password");
        patientDto.setUser(userDto);

        // Mock repository behaviors
        when(patientRepository.existsByEmail(patientDto.getEmail())).thenReturn(false);
        when(patientRepository.existsByNic(patientDto.getNic())).thenReturn(false);

        // Mock userService behaviors
        User user = new User();
        user.setUserId(1L);
        when(userService.createUser(userDto)).thenReturn(user);

        // Mock modelMapper behavior
        Patient patient = new Patient();
        when(modelMapper.map(patientDto, Patient.class)).thenReturn(patient);

        // Mock savedPatient data
        Patient savedPatient = new Patient();
        savedPatient.setPatientId(1L);
        savedPatient.setEmail("test@example.com");
        // Assume other properties are set as well

        // Mock repository save
        when(patientRepository.save(patient)).thenReturn(savedPatient);

        // Mock modelMapper behavior for PatientResponseDto.class
        PatientResponseDto patientResponseDto = new PatientResponseDto();
        when(modelMapper.map(savedPatient, PatientResponseDto.class)).thenReturn(patientResponseDto);

        // Perform the test
        ResponseDto responseDto = patientService.createPatient(patientDto);

        // Verify repository and service method invocations
        verify(patientRepository, times(1)).existsByEmail(patientDto.getEmail());
        verify(patientRepository, times(1)).existsByNic(patientDto.getNic());
        verify(userService, times(1)).createUser(userDto);
        verify(userService, times(1)).assignRoleToUser(user.getUserId(), 4L);
        verify(patientRepository, times(1)).save(patient);
        verify(modelMapper, times(1)).map(savedPatient, PatientResponseDto.class);
        verify(modelMapper, times(1)).map(savedPatient, PatientResponseDto.class);

        // Assert the response
        assertNotNull(responseDto);
        assertEquals(ResponseType.SUCCESS, responseDto.getStatusCode());
        assertEquals(HttpStatus.CREATED, responseDto.getStatus());
        assertEquals("User has been registered successfully!", responseDto.getMessage());
        assertNotNull(responseDto.getData());
        assertTrue(responseDto.getData() instanceof PatientResponseDto);
    }

    @Test
    public void testFetchPatients() {
        // Mock patient data
        Patient patient1 = new Patient();
        patient1.setPatientId(1L);
        patient1.setFirstName("John");
        patient1.setLastName("Doe");

        Patient patient2 = new Patient();
        patient2.setPatientId(2L);
        patient2.setFirstName("Jane");
        patient2.setLastName("Smith");

        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);

        // Mock patientRepository.findAll to return patientList
        when(patientRepository.findAll()).thenReturn(patientList);

        // Mock modelMapper.map to map patients to PatientResponseDto
        PatientResponseDto patientResponseDto1 = new PatientResponseDto();
        patientResponseDto1.setPatientId(1L);
        patientResponseDto1.setFirstName("John");
        patientResponseDto1.setLastName("Doe");

        PatientResponseDto patientResponseDto2 = new PatientResponseDto();
        patientResponseDto2.setPatientId(2L);
        patientResponseDto2.setFirstName("Jane");
        patientResponseDto2.setLastName("Smith");

        List<PatientResponseDto> patientResponseDtoList = new ArrayList<>();
        patientResponseDtoList.add(patientResponseDto1);
        patientResponseDtoList.add(patientResponseDto2);

        when(modelMapper.map(patient1, PatientResponseDto.class)).thenReturn(patientResponseDto1);
        when(modelMapper.map(patient2, PatientResponseDto.class)).thenReturn(patientResponseDto2);

        // Perform the test
        ResponseDto responseDto = patientService.fetchPatients();

        // Verify repository and service method invocations
        verify(patientRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(Patient.class), eq(PatientResponseDto.class));

        // Assert the response
        assertNotNull(responseDto);
        assertEquals(ResponseType.SUCCESS, responseDto.getStatusCode());
        assertEquals(HttpStatus.OK, responseDto.getStatus());
        assertEquals("Success", responseDto.getMessage());
        assertNotNull(responseDto.getData());
        assertTrue(responseDto.getData() instanceof List);
        assertEquals(patientResponseDtoList.size(), ((List<?>) responseDto.getData()).size());
    }
}