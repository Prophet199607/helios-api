package com.helios.api.service;

import com.helios.api.dto.AppointmentDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.Appointment;

public interface AppointmentService {
    Appointment loadAppointmentById(Long appointmentId);

    ResponseDto createAppointment(AppointmentDto appointmentDto);

    ResponseDto loadAppointmentsByPatient(Long patientId);

    ResponseDto loadAppointmentsByNicAndStatus(String patientId, int status);
}
