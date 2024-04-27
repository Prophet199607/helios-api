package com.helios.api.service.impl;

import com.helios.api.dto.AppointmentDto;
import com.helios.api.dto.AppointmentResponseDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.Appointment;
import com.helios.api.repository.AppointmentRepository;
import com.helios.api.service.AppointmentService;
import com.helios.api.util.ResponseType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Appointment loadAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found!"));
    }

    @Override
    public ResponseDto createAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);

        Appointment savedAppointment = appointmentRepository.save(appointment);


        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.CREATED,
                "Appointment has been saved successfully!",
                modelMapper.map(savedAppointment, AppointmentResponseDto.class)
        );
    }
}
