package com.helios.api.service;

import com.helios.api.dto.PatientDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.Patient;

public interface PatientService {
    Patient findPatientByUser(Long userId);

    ResponseDto fetchPatients();

    ResponseDto createPatient(PatientDto patientDto);

    Long getCountOfRecords();
}
