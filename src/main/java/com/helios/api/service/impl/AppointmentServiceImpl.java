package com.helios.api.service.impl;

import com.helios.api.dto.*;
import com.helios.api.entity.Patient;
import com.helios.api.repository.AppointmentRepository;
import com.helios.api.repository.ConsultantRepository;
import com.helios.api.repository.PatientRepository;
import com.helios.api.repository.ScheduleRepository;
import com.helios.api.service.PatientService;
import com.helios.api.util.ResponseType;
import com.helios.api.entity.Appointment;
import com.helios.api.entity.Consultant;
import com.helios.api.service.AppointmentService;
import com.helios.api.service.EmailSenderService;
import com.helios.api.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Appointment loadAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found!"));
    }

    @Override
    public ResponseDto createAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);
        Long consultantId = appointmentDto.getConsultant().getConsultantId();
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new EntityNotFoundException("Consultant not found!"));
        appointment.setConsultant(consultant);
        scheduleRepository.changeScheduleBookedStatus(appointment.getSchedule().getScheduleId(), true);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        Patient jobSeeker = patientRepository.findById(appointmentDto.getPatientDto().getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Job Seeker not found!"));

        // send appointment confirmation email to the jobSeeker
        emailSenderService.sendEmail(jobSeeker.getEmail(),
                "Appointment has been saved successfully",
                "Your appointment is on review. Once the consultant accept your appointment you will get an email");

        // send new appointment placement email to the relevant consultant
        String emailBody = "New appointment received" +
                "\n\r" +
                "click below link to see details " +
                "http://localhost:7000/dashboard/consultant-appointments";

        emailSenderService.sendEmail(consultant.getEmail(),
                "New Appointment Request",
                emailBody);

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.CREATED,
                "Appointment has been saved successfully!",
                modelMapper.map(savedAppointment, AppointmentResponseDto.class)
        );
    }

    @Override
    public ResponseDto createAppointmentByReceptionist(NewJobSeekerRequestDto newJobSeekerRequestDto) {
        ResponseDto patient = patientService.createPatient(modelMapper.map(newJobSeekerRequestDto, PatientDto.class));

//        newJobSeekerRequestDto.setJobSeeker(modelMapper.map(patient.getData(), PatientDto.class));

        return createAppointment(modelMapper.map(newJobSeekerRequestDto, AppointmentDto.class));
    }

    @Override
    public ResponseDto removeAppointment(Long appointmentId) {
        Appointment appointment = loadAppointmentById(appointmentId);
        scheduleRepository.changeScheduleBookedStatus(appointment.getSchedule().getScheduleId(), false);
        appointmentRepository.deleteById(appointmentId);
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "success!",
                null
        );
    }

    @Override
    public List<Appointment> loadAppointmentsByJobSeeker(Long jobSeekerId) {
        return appointmentRepository.findByPatientPatientId(jobSeekerId);
    }

    @Override
    public ResponseDto loadAppointmentsByConsultantId(Long consultantId, int status) {
        List<AppointmentResponseDto> appointments = appointmentRepository.findAppointmentsByConsultantIdAndStatus(consultantId, status)
                .stream().map(appointment -> modelMapper.map(appointment, AppointmentResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "success!",
                appointments
        );
    }

    @Override
    public ResponseDto loadAllAppointments(String startDate, String endDate) {
        if (startDate.equals("0")) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-M-d");
            startDate = currentDate.format(formatter);
            endDate = currentDate.format(formatter);
        }
        List<AppointmentResponseDto> appointments = appointmentRepository.getAllAppointmentsByDates(startDate, endDate)
                .stream().map(appointment -> modelMapper.map(appointment, AppointmentResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "success!",
                appointments
        );
    }

    @Override
    public ResponseDto changeAppointmentStatus(Long appointmentId, int status, boolean isAccepted) {
        appointmentRepository.changeAppointmentStatus(appointmentId, status, isAccepted);
        Appointment appointment = loadAppointmentById(appointmentId);
        if (status == 3) {
            scheduleRepository.changeScheduleBookedStatus(appointment.getSchedule().getScheduleId(), false);
        }
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "success!",
                null
        );
    }

    @Override
    public ResponseDto loadAppointmentsByJobSeeker2(Long jobSeekerId) {
        List<AppointmentResponseDto> appointments = appointmentRepository.findAppointmentsByPatientPatientId(jobSeekerId)
                .stream().map(appointment -> modelMapper.map(appointment, AppointmentResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "success!",
                appointments
        );
    }

    @Override
    public void deleteAllAppointments(List<Appointment> appointments) {
        appointmentRepository.deleteAll(appointments);
    }

    @Override
    public Long getCountOfRecords() {
        return appointmentRepository.count();
    }
}
