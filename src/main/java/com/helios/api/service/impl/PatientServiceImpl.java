package com.helios.api.service.impl;

import com.helios.api.dto.*;
import com.helios.api.entity.Patient;
import com.helios.api.entity.Patient;
import com.helios.api.entity.User;
import com.helios.api.repository.PatientRepository;
import com.helios.api.service.PatientService;
import com.helios.api.service.UserService;
import com.helios.api.util.ResponseType;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserService userService;
    @Override
    public Patient findPatientById(Long patientId) {
        return null;
    }

    @Override
    public Patient findConsultantByUser(Long userId) {
        return null;
    }

    @Override
    public ResponseDto fetchPatientById(Long patientId) {
        return null;
    }

    @Override
    public ResponseDto fetchPatients() {
        List<PatientResponseDto> patients = patientRepository.findAll()
                .stream().map(patient -> modelMapper.map(patient, PatientResponseDto.class))
                .toList();
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success",
                patients
        );
    }

    @Override
    public ResponseDto fetchAllPatientsWithPagination(int page, int size) {
        return null;
    }

    @Override
    public ResponseDto findPatientsByName(String name, int page, int size) {
        return null;
    }

    @Override
    public ResponseDto loadPatientByEmail(String email) {
        return null;
    }

    @Override
    public ResponseDto createPatient(PatientDto patientDto) {
        if (patientRepository.existsByEmail(patientDto.getEmail())) {
            return new ResponseDto(ResponseType.DUPLICATE_ENTRY, HttpStatus.CONFLICT, "Duplicate email found!", null);
        }

        StringBuilder tempPassword = new StringBuilder();
        if (patientDto.getUser().getPassword() == null) {
            int length = 5;
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

            Random random = new Random();
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(characters.length());
                char randomChar = characters.charAt(randomIndex);
                tempPassword.append(randomChar);
            }
            patientDto.getUser().setPassword(tempPassword.toString());

            // send temp password email to the jobSeeker
//            emailSenderService.sendEmail(patientDto.getEmail(),
//                    "Welcome!",
//                    "Hello! " + patientDto.getUser().getFullName() + "\nYour temporary password is: " + tempPassword + "\n" + "Use your first name as username");
        }

        User user = userService.createUser(new UserDto(patientDto.getUser().getUserName().toLowerCase(),
                patientDto.getUser().getFullName(),
                patientDto.getUser().getEmail(), patientDto.getUser().getPassword()));

        userService.assignRoleToUser(user.getUserId(), 4L);

        Patient jobSeeker = modelMapper.map(patientDto, Patient.class);
        jobSeeker.setUser(user);

        Patient savedPatient = patientRepository.save(jobSeeker);

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.CREATED,
                "Patient has been saved successfully!",
                modelMapper.map(savedPatient, PatientResponseDto.class)
        );
    }

    @Override
    public ResponseDto updatePatient(PatientDto patientDto) {
        return null;
    }

    @Override
    public ResponseDto removePatient(Long patientId) {
        return null;
    }

    @Override
    public Long getCountOfRecords() {
        return null;
    }
}
