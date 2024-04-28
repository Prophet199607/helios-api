package com.helios.api.controller;

import com.helios.api.dto.PatientDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/register")
public class RegisterController {
    @Autowired
    private PatientService patientService;
    @PostMapping
    public ResponseEntity<ResponseDto> createPatient(@RequestBody PatientDto patientDto) {
        ResponseDto responseDto = patientService.createPatient(patientDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
