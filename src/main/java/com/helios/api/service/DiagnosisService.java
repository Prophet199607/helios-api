package com.helios.api.service;

import com.helios.api.dto.DiagnosisDto;
import com.helios.api.dto.ResponseDto;

public interface DiagnosisService {
    ResponseDto createDiagnosis(DiagnosisDto diagnosisDto);

    ResponseDto loadDiagnosesByPatient(Long patientId);

}
