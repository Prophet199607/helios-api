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
    private final DiagnosisService diagnosisService;

    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> createDiagnosis(@RequestBody DiagnosisDto diagnosisDto) {
        ResponseDto responseDto = diagnosisService.createDiagnosis(diagnosisDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_DOCTOR', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> loadDiagnosesByPatient(@PathVariable Long patientId) {
        ResponseDto responseDto = diagnosisService.loadDiagnosesByPatient(patientId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
