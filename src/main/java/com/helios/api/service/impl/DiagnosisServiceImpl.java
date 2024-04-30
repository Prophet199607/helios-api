package com.helios.api.service.impl;

import com.helios.api.dto.DiagnosisDto;
import com.helios.api.dto.DiagnosisResponseDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.Diagnosis;
import com.helios.api.repository.AppointmentRepository;
import com.helios.api.repository.DiagnosisRepository;
import com.helios.api.service.DiagnosisService;
import com.helios.api.util.ResponseType;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiagnosisServiceImpl implements DiagnosisService {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ResponseDto createDiagnosis(DiagnosisDto diagnosisDto) {
        Diagnosis diagnosis = modelMapper.map(diagnosisDto, Diagnosis.class);

        Diagnosis savedDiagnosis = diagnosisRepository.save(diagnosis);

        appointmentRepository.changeAppointmentStatus(diagnosisDto.getAppointment().getAppointmentId(), 1);

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.CREATED,
                "Diagnosis has been saved successfully!",
                modelMapper.map(savedDiagnosis, DiagnosisResponseDto.class)
        );
    }

    @Override
    public ResponseDto loadDiagnosesByPatient(Long patientId) {
        List<DiagnosisResponseDto> diagnoses = diagnosisRepository.findDiagnosesByPatientPatientId(patientId)
                .stream().map(diagnosis -> modelMapper.map(diagnosis, DiagnosisResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "success!",
                diagnoses
        );
    }
}
