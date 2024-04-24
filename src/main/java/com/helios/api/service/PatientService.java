package com.helios.api.service;

import com.helios.api.dto.PatientDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.Patient;

public interface PatientService {
    Patient findPatientById(Long patientId);

    Patient findPatientByUser(Long userId);

    ResponseDto fetchPatientById(Long patientId);

    ResponseDto fetchPatients();

    ResponseDto fetchAllPatientsWithPagination(int page, int size);

    ResponseDto findPatientsByName(String name, int page, int size);

    ResponseDto loadPatientByEmail(String email);

    ResponseDto createPatient(PatientDto patientDto);

    ResponseDto updatePatient(PatientDto patientDto);

    ResponseDto removePatient(Long patientId);

    Long getCountOfRecords();
}
