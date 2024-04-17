package com.helios.api.service.impl;

import com.helios.api.dto.ConsultantDto;
import com.helios.api.dto.ConsultantResponseDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.dto.UserDto;
import com.helios.api.entity.*;
import com.helios.api.mapper.ConsultantMapper;
import com.helios.api.repository.AppointmentRepository;
import com.helios.api.repository.ConsultantRepository;
import com.helios.api.repository.ScheduleRepository;
import com.helios.api.service.AppointmentService;
import com.helios.api.service.ConsultantService;
import com.helios.api.service.EmailSenderService;
import com.helios.api.service.UserService;
import com.helios.api.util.ResponseType;
import com.helios.api.entity.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConsultantServiceImpl implements ConsultantService {

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ConsultantMapper consultantMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public Consultant findConsultantById(Long consultantId) {
        return consultantRepository.findById(consultantId).orElse(null);
    }

    @Override
    public Consultant findConsultantByUser(Long userId) {
        return consultantRepository.findConsultantByUserId(userId);
    }

    @Override
    public ResponseDto fetchConsultantById(Long consultantId) {
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new EntityNotFoundException("Consultant with ID " + consultantId + " not found"));
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success",
                modelMapper.map(consultant, ConsultantResponseDto.class)
        );
    }

    @Override
    public ResponseDto fetchConsultants() {
        List<ConsultantResponseDto> consultants = consultantRepository.findAll()
                .stream().map(consultant -> modelMapper.map(consultant, ConsultantResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success",
                consultants
        );
    }

    @Override
    public ResponseDto fetchAllConsultantsWithPagination(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Consultant> allConsultantsWithPagination = consultantRepository.getAllConsultantsWithPagination(pageRequest);

        PageImpl<ConsultantResponseDto> consultantResponseDtos = new PageImpl<>(allConsultantsWithPagination.getContent().stream()
                .map(consultant -> modelMapper.map(consultant, ConsultantResponseDto.class))
                .collect(Collectors.toList()), pageRequest, allConsultantsWithPagination.getTotalElements());

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success",
                consultantResponseDtos
        );
    }

    @Override
    public ResponseDto findConsultantsByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Consultant> consultantsPage = consultantRepository.findConsultantsByFirstName(name, pageRequest);

        PageImpl<ConsultantResponseDto> consultantSearchData = new PageImpl<>(consultantsPage.getContent().stream()
                .map(consultant -> modelMapper.map(consultant, ConsultantResponseDto.class))
                .collect(Collectors.toList()), pageRequest, consultantsPage.getTotalElements());

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success",
                consultantSearchData
        );
    }


    @Override
    public ResponseDto loadConsultantByEmail(String email) {
        Consultant consultant = consultantRepository.findConsultantsByEmail(email);
        if (consultant == null) return new ResponseDto(ResponseType.DATA_NOT_FOUND,
                HttpStatus.NOT_FOUND, "No consultant found with " + email + "!", null);
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success!",
                modelMapper.map(consultant, ConsultantResponseDto.class)
        );
    }

    @Override
    public ResponseDto createConsultant(Consultant consultant) {
        /* check whether the email is duplicate or not in the user table **/
        User loadedUser = userService.loadUserByEmail(consultant.getUser().getEmail());
        if (loadedUser != null) {
            return new ResponseDto(ResponseType.DUPLICATE_ENTRY, HttpStatus.CONFLICT, "Duplicate user email found!", null);
        }

        if (consultantRepository.existsByEmail(consultant.getEmail())) {
            return new ResponseDto(ResponseType.DUPLICATE_ENTRY, HttpStatus.CONFLICT, "Duplicate email found!", null);
        }

        StringBuilder tempPassword = new StringBuilder();
        if (consultant.getUser().getPassword() == null) {
            int length = 5;
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

            Random random = new Random();
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(characters.length());
                char randomChar = characters.charAt(randomIndex);
                tempPassword.append(randomChar);
            }

            consultant.getUser().setPassword(tempPassword.toString());

            // send temp password email to consultant
            emailSenderService.sendEmail(consultant.getUser().getEmail(),
                    "Welcome!",
                    "Hello! " + consultant.getUser().getFullName() + "\nYour temporary password is: " + tempPassword + "\nUse your first name as username");
        }

        User user = userService.createUser(new UserDto(consultant.getUser().getUsername(),
                consultant.getUser().getFullName(),
                consultant.getUser().getEmail(), consultant.getUser().getPassword()));

        userService.assignRoleToUser(user.getUserId(), 2L);

//        Consultant consultant = consultantMapper.fromConsultantDto(consultantDto);
        consultant.setUser(user);
        JobType jobType = modelMapper.map(consultant.getJobType(), JobType.class);
        consultant.setJobType(jobType);

        Consultant savedConsultant = consultantRepository.save(consultant);

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.CREATED,
                "Consultant has been saved successfully!",
                modelMapper.map(savedConsultant, ConsultantResponseDto.class)
        );
    }

    @Override
    public ResponseDto updateConsultant(ConsultantDto consultantDto) {
        Consultant loadedConsultant = findConsultantById(consultantDto.getConsultantId());
        Consultant consultant = modelMapper.map(consultantDto, Consultant.class);
//        consultant.setJobType(loadedConsultant.getJobType());
//        consultant.setCountry(loadedConsultant.getCountry());
        consultant.setUser(loadedConsultant.getUser());
        Consultant updatedConsultant = consultantRepository.save(consultant);
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Consultant has been updated successfully!",
                modelMapper.map(updatedConsultant, ConsultantResponseDto.class)
        );
    }

    @Override
    public ResponseDto removeConsultant(Long consultantId) {
        Consultant consultant = findConsultantById(consultantId);
        if (consultant == null) return new ResponseDto(ResponseType.DATA_NOT_FOUND,
                HttpStatus.NOT_FOUND, "No consultant found with ID " + consultantId + "!", null);

        for (Appointment appointment : consultant.getAppointments()) {
            appointmentService.removeAppointment(appointment.getAppointmentId());
        }
        consultantRepository.deleteById(consultantId);

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Consultant has been deleted successfully!",
                modelMapper.map(consultant, ConsultantResponseDto.class)
        );

    }

    @Override
    public Long getCountOfRecords() {
        return consultantRepository.count();
    }

    @Override
    public Long getScheduleCount(Long consultantId) {
        return scheduleRepository.countScheduleByConsultantConsultantId(consultantId);
    }

    @Override
    public Long getAppointmentsCount(Long consultantId) {
        return appointmentRepository.countAppointmentByConsultantConsultantId(consultantId);
    }

    @Override
    public Long getNewAppointmentsCount(Long consultantId) {
        return appointmentRepository.countAppointmentByConsultantConsultantIdAndStatus(consultantId, 0);
    }
}
