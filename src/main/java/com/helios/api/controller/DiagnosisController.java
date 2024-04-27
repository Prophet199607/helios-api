package com.helios.api.controller;

import com.helios.api.dto.DiagnosisDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/diagnosis")
public class DiagnosisController {
    @Autowired
    private DiagnosisService diagnosisService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> createConsultant(@RequestBody DiagnosisDto diagnosisDto) {
        ResponseDto responseDto = diagnosisService.createDiagnosis(diagnosisDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_DOCTOR', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> loadAppointmentsByPatient(@PathVariable Long patientId) {
        ResponseDto responseDto = diagnosisService.loadDiagnosesByPatient(patientId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
