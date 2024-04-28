package com.helios.api.service.impl;

import com.helios.api.dto.AppointmentDto;
import com.helios.api.dto.AppointmentResponseDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.Appointment;
import com.helios.api.repository.AppointmentRepository;
import com.helios.api.util.ResponseType;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private AppointmentServiceImpl appointmentService;
    @Mock
    private ModelMapper modelMapper;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testLoadAppointmentById_ExistingAppointmentId() {
        Long appointmentId = 1L;
        Appointment expectedAppointment = new Appointment();
        expectedAppointment.setAppointmentId(appointmentId);
        expectedAppointment.setStatus(0);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(expectedAppointment));

        Appointment resultAppointment = appointmentService.loadAppointmentById(appointmentId);

        assertEquals(expectedAppointment.getAppointmentId(), resultAppointment.getAppointmentId());
        assertEquals(expectedAppointment.getStatus(), resultAppointment.getStatus());
    }

    @Test
    public void testLoadAppointmentById_NonExistingAppointmentId() {
        Long appointmentId = 2L;
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> appointmentService.loadAppointmentById(appointmentId));
    }

    @Test
    public void testCreateAppointment_Success() {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setStatus(0);

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setStatus(appointmentDto.getStatus());

        AppointmentResponseDto expectedResponseDto = new AppointmentResponseDto();
        expectedResponseDto.setAppointmentId(1L);
        expectedResponseDto.setStatus(appointment.getStatus());

        when(modelMapper.map(appointmentDto, Appointment.class)).thenReturn(appointment);
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(modelMapper.map(appointment, AppointmentResponseDto.class)).thenReturn(expectedResponseDto);

        ResponseDto responseDto = appointmentService.createAppointment(appointmentDto);

        assertNotNull(responseDto);
        assertEquals(ResponseType.SUCCESS, responseDto.getStatusCode());
        assertEquals(HttpStatus.CREATED, responseDto.getStatus());
        assertEquals("Appointment has been saved successfully!", responseDto.getMessage());
        assertEquals(expectedResponseDto, responseDto.getData());
    }

    @Test
    public void testCreateAppointment_Failure() {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setStatus(0);

        when(modelMapper.map(appointmentDto, Appointment.class)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(appointmentDto));
    }

    @Test
    public void testLoadAppointmentsByPatient_Success() {
        Long patientId = 1L;

        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentId(1L);
        appointment1.setStatus(0);

        Appointment appointment2 = new Appointment();
        appointment2.setAppointmentId(2L);
        appointment2.setStatus(0);

        List<Appointment> expectedAppointments = Arrays.asList(appointment1, appointment2);

        AppointmentResponseDto expectedResponseDto1 = new AppointmentResponseDto();
        expectedResponseDto1.setAppointmentId(1L);
        expectedResponseDto1.setStatus(appointment1.getStatus());

        AppointmentResponseDto expectedResponseDto2 = new AppointmentResponseDto();
        expectedResponseDto2.setAppointmentId(2L);
        expectedResponseDto2.setStatus(appointment2.getStatus());

        when(appointmentRepository.findAppointmentsByPatientPatientId(patientId)).thenReturn(expectedAppointments);
        when(modelMapper.map(appointment1, AppointmentResponseDto.class)).thenReturn(expectedResponseDto1);
        when(modelMapper.map(appointment2, AppointmentResponseDto.class)).thenReturn(expectedResponseDto2);

        ResponseDto responseDto = appointmentService.loadAppointmentsByPatient(patientId);

        assertNotNull(responseDto);
        assertEquals(ResponseType.SUCCESS, responseDto.getStatusCode());
        assertEquals(HttpStatus.OK, responseDto.getStatus());
        assertEquals("success!", responseDto.getMessage());

        List<AppointmentResponseDto> actualAppointments = (List<AppointmentResponseDto>) responseDto.getData();
        assertNotNull(actualAppointments);
        assertEquals(2, actualAppointments.size());
        assertEquals(expectedResponseDto1.getAppointmentId(), actualAppointments.get(0).getAppointmentId());
        assertEquals(expectedResponseDto1.getStatus(), actualAppointments.get(0).getStatus());
        assertEquals(expectedResponseDto2.getAppointmentId(), actualAppointments.get(1).getAppointmentId());
        assertEquals(expectedResponseDto2.getStatus(), actualAppointments.get(1).getStatus());
    }

    @Test
    public void testLoadAppointmentsByPatient_EmptyList() {
        Long patientId = 2L;
        when(appointmentRepository.findAppointmentsByPatientPatientId(patientId)).thenReturn(Arrays.asList());

        ResponseDto responseDto = appointmentService.loadAppointmentsByPatient(patientId);

        assertNotNull(responseDto);
        assertEquals(ResponseType.SUCCESS, responseDto.getStatusCode());
        assertEquals(HttpStatus.OK, responseDto.getStatus());
        assertEquals("success!", responseDto.getMessage());

        List<AppointmentResponseDto> actualAppointments = (List<AppointmentResponseDto>) responseDto.getData();
        assertNotNull(actualAppointments);
        assertEquals(0, actualAppointments.size());
    }

    @Test
    public void testLoadAppointmentsByNicAndStatus_Success() {
        String patientNic = "123456789";
        int status = 1;

        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentId(1L);
        appointment1.setStatus(0);

        Appointment appointment2 = new Appointment();
        appointment2.setAppointmentId(2L);
        appointment2.setStatus(0);

        List<Appointment> expectedAppointments = Arrays.asList(appointment1, appointment2);

        AppointmentResponseDto expectedResponseDto1 = new AppointmentResponseDto();
        expectedResponseDto1.setAppointmentId(1L);
        expectedResponseDto1.setStatus(appointment1.getStatus());

        AppointmentResponseDto expectedResponseDto2 = new AppointmentResponseDto();
        expectedResponseDto2.setAppointmentId(2L);
        expectedResponseDto2.setStatus(appointment2.getStatus());

        when(appointmentRepository.findAppointmentsByPatientNicAndStatus(patientNic, status)).thenReturn(expectedAppointments);
        when(modelMapper.map(appointment1, AppointmentResponseDto.class)).thenReturn(expectedResponseDto1);
        when(modelMapper.map(appointment2, AppointmentResponseDto.class)).thenReturn(expectedResponseDto2);

        ResponseDto responseDto = appointmentService.loadAppointmentsByNicAndStatus(patientNic, status);

        assertNotNull(responseDto);
        assertEquals(ResponseType.SUCCESS, responseDto.getStatusCode());
        assertEquals(HttpStatus.OK, responseDto.getStatus());
        assertEquals("success!", responseDto.getMessage());

        List<AppointmentResponseDto> actualAppointments = (List<AppointmentResponseDto>) responseDto.getData();
        assertNotNull(actualAppointments);
        assertEquals(2, actualAppointments.size());
        assertEquals(expectedResponseDto1.getAppointmentId(), actualAppointments.get(0).getAppointmentId());
        assertEquals(expectedResponseDto1.getStatus(), actualAppointments.get(0).getStatus());
        assertEquals(expectedResponseDto2.getAppointmentId(), actualAppointments.get(1).getAppointmentId());
        assertEquals(expectedResponseDto2.getStatus(), actualAppointments.get(1).getStatus());
    }

    @Test
    public void testLoadAppointmentsByNicAndStatus_EmptyList() {
        String patientNic = "123456789";
        int status = 2;
        when(appointmentRepository.findAppointmentsByPatientNicAndStatus(patientNic, status)).thenReturn(Arrays.asList());

        ResponseDto responseDto = appointmentService.loadAppointmentsByNicAndStatus(patientNic, status);

        assertNotNull(responseDto);
        assertEquals(ResponseType.SUCCESS, responseDto.getStatusCode());
        assertEquals(HttpStatus.OK, responseDto.getStatus());
        assertEquals("success!", responseDto.getMessage());

        List<AppointmentResponseDto> actualAppointments = (List<AppointmentResponseDto>) responseDto.getData();
        assertNotNull(actualAppointments);
        assertEquals(0, actualAppointments.size());
    }
}