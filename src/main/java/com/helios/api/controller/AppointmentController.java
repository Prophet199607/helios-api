package com.helios.api.controller;

import com.helios.api.dto.AppointmentDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> createConsultant(@RequestBody AppointmentDto appointmentDto) {
        ResponseDto responseDto = appointmentService.createAppointment(appointmentDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
