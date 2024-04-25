package com.helios.api.runner;

import com.helios.api.dto.PatientDto;
import com.helios.api.entity.*;
import com.helios.api.service.*;
import com.helios.api.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private StaffMemberService staffMemberService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void run(String... args) throws Exception {
        createUserType();
        createRoles();
        createAdmin();
        createStaffMember();
        createPatient();
    }

    private void createUserType() {
        Arrays.asList("Doctor", "Laboratory")
                .forEach(userType -> userTypeService.createUserType(userType, 1));
    }

    private void createStaffMember() {
        StaffMember staffMemberDto = new StaffMember();
        staffMemberDto.setEmail("pasinduxx@hotmail.com");
        staffMemberDto.setFirstName("John");
        staffMemberDto.setLastName("Doe");
        staffMemberDto.setContactNumber("0712381996");
        staffMemberDto.setIsAvailable(true);

        UserType userType = userTypeService.loadUserTypeById(1L);
        staffMemberDto.setUserType(userType);

        User userDto = new User();
        userDto.setUserName("john");
        userDto.setFullName("John Doe");
        userDto.setEmail("pasinduxx@hotmail.com");
        userDto.setPassword("1234");
        staffMemberDto.setUser(userDto);

        staffMemberService.createStaffMember(staffMemberDto);
    }

    private void createRoles() {

        Arrays.asList("ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_LABORATORY", "ROLE_USER")
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
