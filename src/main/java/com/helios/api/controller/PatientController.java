package com.helios.api.controller;

import com.helios.api.dto.PatientDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.service.AppointmentService;
import com.helios.api.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> findAllPatients() {
        ResponseDto responseDto = patientService.fetchPatients();
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }


    @PostMapping
    public ResponseEntity<ResponseDto> createPatient(@RequestBody PatientDto patientDto) {
        ResponseDto responseDto = patientService.createPatient(patientDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }



    @GetMapping("/find")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_LABORATORY')")
    public ResponseEntity<ResponseDto> loadAppointmentsByNicAndStatus(
            @RequestParam(name = "nic", defaultValue = "") String nic,
            @RequestParam(name = "status", defaultValue = "1") int status
    ) {
        ResponseDto responseDto = appointmentService.loadAppointmentsByNicAndStatus(nic, status);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

}
