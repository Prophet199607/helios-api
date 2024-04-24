package com.helios.api.runner;

import com.helios.api.dto.PatientDto;
import com.helios.api.entity.*;
import com.helios.api.dto.AppointmentDto;
import com.helios.api.mapper.JobTypeMapper;
import com.helios.api.service.*;
import com.helios.api.dto.ConsultantDto;
import com.helios.api.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    private JobTypeService jobTypeService;

    @Autowired
    private ConsultantService consultantService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JobTypeMapper jobTypeMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void run(String... args) throws Exception {
        createJobTypes();
        createRoles();
        createAdmin();
//        createConsultant();
        createPatient();
//        createAppointment();

    }

    private void createJobTypes() {
        Arrays.asList("Software Engineering", "Mechanic", "Web Developer", "Plumber", "Electrical Technician")
                .forEach(jobType -> jobTypeService.createJobType(jobType, 1));
    }

    private void createConsultant() {
        Consultant consultantDto = new Consultant();
        consultantDto.setEmail("pasinduxx@hotmail.com");
        consultantDto.setFirstName("John");
        consultantDto.setLastName("Doe");
        consultantDto.setContactNumber("0712381996");
        consultantDto.setIsAvailable(true);

        JobType jobType = jobTypeService.loadJobTypeById(1L);
        consultantDto.setJobType(jobType);

        User userDto = new User();
        userDto.setUserName("john");
        userDto.setFullName("John Doe");
        userDto.setEmail("pasinduxx@hotmail.com");
        userDto.setPassword("1234");
        consultantDto.setUser(userDto);

        consultantService.createConsultant(consultantDto);
    }

    private void createAppointment() {
        AppointmentDto appointmentDto = new AppointmentDto();
//        appointmentDto.setAppointmentDate(LocalDate.now());
//        appointmentDto.setTimeFrom(LocalTime.now());
//        appointmentDto.setTimeTo(LocalTime.now().plusHours(1));
        appointmentDto.setStatus(0);
        appointmentDto.setIsAccepted(false);

        Consultant consultant = consultantService.findConsultantById(1L);
        appointmentDto.setConsultant(modelMapper.map(consultant, ConsultantDto.class));

        Patient jobSeeker = patientService.findPatientById(1L);
        appointmentDto.setPatientDto(modelMapper.map(jobSeeker, PatientDto.class));

        appointmentService.createAppointment(appointmentDto);
    }

    private void createRoles() {

        Arrays.asList("ROLE_ADMIN", "ROLE_CONSULTANT", "ROLE_RECEPTIONIST", "ROLE_USER")
                .forEach(role -> roleService.createRole(role));
    }

    private void createAdmin() {
        User admin = userService.createUser(new UserDto("admin", "Administrator", "admin@gmail.com", "1234"));
        userService.assignRoleToUser(admin.getUserId(), 1L);
    }

    private void createPatient() {
        PatientDto patientDto = new PatientDto();
        patientDto.setEmail("dev7@onimtait.com");
        patientDto.setFirstName("Tom");
        patientDto.setLastName("Riddle");
        patientDto.setContactNumber("0712381996");
        patientDto.setNic("199620200917");
        patientDto.setAddress1("No.12");
        patientDto.setAddress2("Homagama");
        patientDto.setIsActive(true);

        UserDto userDto = new UserDto();
        userDto.setUserName("tom");
        userDto.setFullName("Tom Riddle");
        userDto.setEmail("dev7@onimtait.com");
        userDto.setPassword("asd");
        patientDto.setUser(userDto);

        patientService.createPatient(patientDto);

    }

}
