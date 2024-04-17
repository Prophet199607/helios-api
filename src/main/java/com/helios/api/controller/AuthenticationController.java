package com.helios.api.controller;


import com.helios.api.dto.AuthRequest;
import com.helios.api.dto.AuthResponseDto;
import com.helios.api.dto.UserResponseDto;
import com.helios.api.entity.Consultant;
import com.helios.api.entity.Patient;
import com.helios.api.entity.User;
import com.helios.api.repository.UserRepository;
import com.helios.api.service.ConsultantService;
import com.helios.api.service.JwtService;
import com.helios.api.service.PatientService;
import com.helios.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsultantService consultantService;

    @Autowired
    private PatientService patientService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDto> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.getUsername());
            AuthResponseDto authResponseDto = new AuthResponseDto();
            UserResponseDto loggedInUser = userService.findUserByUserName(authRequest.getUsername());
            Optional<User> byUserName = userRepository.findByUserName(authRequest.getUsername());
            authResponseDto.setToken(token);
            authResponseDto.setUser(loggedInUser);

            List<String> roles = byUserName.get().getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            String role = roles.get(0);
            if (role.equals("ROLE_CONSULTANT")) {
                Consultant consultantByUser = consultantService.findConsultantByUser(loggedInUser.getUserId());
                authResponseDto.setLoggedUserId(consultantByUser.getConsultantId());
            }
            if (role.equals("ROLE_USER")) {
                Patient jobSeekerByUser = patientService.findConsultantByUser(loggedInUser.getUserId());
                authResponseDto.setLoggedUserId(jobSeekerByUser.getPatientId());
            }

            authResponseDto.setRoles(roles);
            return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("Invalid username or password!");
        }
    }
}
