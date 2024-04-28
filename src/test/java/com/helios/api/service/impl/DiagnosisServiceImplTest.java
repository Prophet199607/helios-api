package com.helios.api.service.impl;

import com.helios.api.dto.AppointmentDto;
import com.helios.api.dto.DiagnosisDto;
import com.helios.api.dto.DiagnosisResponseDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.Diagnosis;
import com.helios.api.entity.Patient;
import com.helios.api.repository.AppointmentRepository;
import com.helios.api.repository.DiagnosisRepository;
import com.helios.api.util.ResponseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiagnosisServiceImplTest {
    @InjectMocks
    private DiagnosisServiceImpl diagnosisService;

    @Mock
    private DiagnosisRepository diagnosisRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ModelMapper modelMapper;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDiagnosis() {
        DiagnosisDto diagnosisDto = new DiagnosisDto();
        diagnosisDto.setAppointment(new AppointmentDto());
        Diagnosis diagnosis = new Diagnosis();
        Diagnosis savedDiagnosis = new Diagnosis();

        when(modelMapper.map(diagnosisDto, Diagnosis.class)).thenReturn(diagnosis);
        when(diagnosisRepository.save(diagnosis)).thenReturn(savedDiagnosis);

        ResponseDto responseDto = diagnosisService.createDiagnosis(diagnosisDto);

        assertEquals(ResponseType.SUCCESS, responseDto.getStatusCode());
        assertEquals(HttpStatus.CREATED, responseDto.getStatus());
        assertEquals("Diagnosis has been saved successfully!", responseDto.getMessage());
        verify(appointmentRepository, times(1)).changeAppointmentStatus(diagnosisDto.getAppointment().getAppointmentId(), 1);
    }

    @Test
    public void testLoadDiagnosesByPatient() {
        Patient patient1 = new Patient();
        patient1.setPatientId(1L);

        Diagnosis diagnosis1 = new Diagnosis();
        diagnosis1.setDiagnosisId(1L);
        diagnosis1.setReportStatus("Severe");
        diagnosis1.setPatient(patient1);

        Diagnosis diagnosis2 = new Diagnosis();
        diagnosis2.setDiagnosisId(2L);
        diagnosis2.setReportStatus("Moderate");
        diagnosis1.setPatient(patient1);

        List<Diagnosis> expectedDiagnoses = Arrays.asList(diagnosis1, diagnosis2);
        when(diagnosisRepository.findDiagnosesByPatientPatientId(patient1.getPatientId())).thenReturn(expectedDiagnoses);

        ResponseDto responseDto = diagnosisService.loadDiagnosesByPatient(patient1.getPatientId());

        assertNotNull(responseDto);
        assertEquals(ResponseType.SUCCESS, responseDto.getStatusCode());
        assertEquals(HttpStatus.OK, responseDto.getStatus());
        assertEquals("success!", responseDto.getMessage());

        List<DiagnosisResponseDto> actualDiagnoses = (List<DiagnosisResponseDto>) responseDto.getData();
        assertNotNull(actualDiagnoses);
        assertEquals(2, actualDiagnoses.size());

    }
}