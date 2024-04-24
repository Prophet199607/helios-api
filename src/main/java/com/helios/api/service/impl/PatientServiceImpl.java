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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    public Patient findPatientByUser(Long userId) {
        return patientRepository.findPatientByUserId(userId);
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
            return new ResponseDto(ResponseType.DUPLICATE_ENTRY, HttpStatus.CONFLICT, "Duplicate email found!",
                    null);
        }

        if (patientRepository.existsByNic(patientDto.getNic())) {
            return new ResponseDto(ResponseType.DUPLICATE_ENTRY, HttpStatus.CONFLICT, "Duplicate NIC found!",
                    null);
        }


        Patient patient = modelMapper.map(patientDto, Patient.class);

        try {
            Date birthday = extractBirthdayFromNIC(patientDto.getNic());
            String gender = extractGenderFromNIC(patientDto.getNic());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            patient.setBirthday(dateFormat.format(birthday));
            patient.setGender(gender);
        } catch (NullPointerException e) {
            return new ResponseDto(
                    ResponseType.FAIL,
                    HttpStatus.BAD_REQUEST,
                    "Invalid NIC!",
                    null
            );
        }

        User user = userService.createUser(new UserDto(patientDto.getUser().getUserName().toLowerCase(),
                patientDto.getUser().getFullName(),
                patientDto.getUser().getEmail(), patientDto.getUser().getPassword()));

        userService.assignRoleToUser(user.getUserId(), 4L);

        patient.setUser(user);

        Patient savedPatient = patientRepository.save(patient);

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.CREATED,
                "User has been registered successfully!",
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


    public static Date extractBirthdayFromNIC(String nicNumber) {

        if (nicNumber.length() == 12) {
            int year = Integer.parseInt(nicNumber.substring(0, 4));
            int daysUntilBirthday = Integer.parseInt(nicNumber.substring(4, 7));

            boolean isFemale = daysUntilBirthday > 500;
            if (isFemale) {
                daysUntilBirthday -= 500;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.DAY_OF_YEAR, daysUntilBirthday);
            return calendar.getTime();
        } else if (nicNumber.length() == 10) {
            int year = 1900 + Integer.parseInt(nicNumber.substring(0, 2));
            int daysUntilBirthday = Integer.parseInt(nicNumber.substring(2, 5));

            boolean isFemale = daysUntilBirthday > 500;
            if (isFemale) {
                daysUntilBirthday -= 500;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.DAY_OF_YEAR, daysUntilBirthday);
            return calendar.getTime();
        } else {
            return null;
        }
    }

    public static String extractGenderFromNIC(String nicNumber) {
        if (nicNumber.length() == 12) {
            return nicNumber.charAt(9) % 2 == 0 ? "Female" : "Male";
        } else if (nicNumber.length() == 10 && Character.isLetter(nicNumber.charAt(9))) {
            return Character.toUpperCase(nicNumber.charAt(9)) == 'V' ? "Female" : "Male";
        } else {
            return null;
        }
    }
}
