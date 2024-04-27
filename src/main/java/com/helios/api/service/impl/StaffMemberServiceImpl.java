package com.helios.api.service.impl;

import com.helios.api.dto.StaffMemberDto;
import com.helios.api.dto.StaffMemberResponseDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.dto.UserDto;
import com.helios.api.entity.*;
import com.helios.api.mapper.StaffMemberMapper;
import com.helios.api.repository.StaffMemberRepository;
import com.helios.api.service.StaffMemberService;
import com.helios.api.service.EmailSenderService;
import com.helios.api.service.UserService;
import com.helios.api.util.ResponseType;
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
public class StaffMemberServiceImpl implements StaffMemberService {

    @Autowired
    private StaffMemberRepository staffMemberRepository;

    @Autowired
    private StaffMemberMapper staffMemberMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public StaffMember findStaffMemberById(Long staffMemberId) {
        return staffMemberRepository.findById(staffMemberId).orElse(null);
    }

    @Override
    public StaffMember findStaffMemberByUser(Long userId) {
        return staffMemberRepository.findStaffMemberByUserId(userId);
    }

    @Override
    public ResponseDto fetchStaffMemberById(Long staffMemberId) {
        StaffMember staffMember = staffMemberRepository.findById(staffMemberId)
                .orElseThrow(() -> new EntityNotFoundException("StaffMember with ID " + staffMemberId + " not found"));
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success",
                modelMapper.map(staffMember, StaffMemberResponseDto.class)
        );
    }

    @Override
    public ResponseDto fetchStaffMembers() {
        List<StaffMemberResponseDto> staffMembers = staffMemberRepository.findAll()
                .stream().map(staffMember -> modelMapper.map(staffMember, StaffMemberResponseDto.class))
                .collect(Collectors.toList());
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success",
                staffMembers
        );
    }

    @Override
    public ResponseDto fetchAllStaffMembersWithPagination(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<StaffMember> allStaffMembersWithPagination = staffMemberRepository.getAllStaffMembersWithPagination(pageRequest);

        PageImpl<StaffMemberResponseDto> staffMemberResponseDtos = new PageImpl<>(allStaffMembersWithPagination.getContent().stream()
                .map(staffMember -> modelMapper.map(staffMember, StaffMemberResponseDto.class))
                .collect(Collectors.toList()), pageRequest, allStaffMembersWithPagination.getTotalElements());

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success",
                staffMemberResponseDtos
        );
    }

    @Override
    public ResponseDto findStaffMembersByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<StaffMember> staffMembersPage = staffMemberRepository.findStaffMembersByFirstName(name, pageRequest);

        PageImpl<StaffMemberResponseDto> staffMemberSearchData = new PageImpl<>(staffMembersPage.getContent().stream()
                .map(staffMember -> modelMapper.map(staffMember, StaffMemberResponseDto.class))
                .collect(Collectors.toList()), pageRequest, staffMembersPage.getTotalElements());

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success",
                staffMemberSearchData
        );
    }


    @Override
    public ResponseDto loadStaffMemberByEmail(String email) {
        StaffMember staffMember = staffMemberRepository.findStaffMembersByEmail(email);
        if (staffMember == null) return new ResponseDto(ResponseType.DATA_NOT_FOUND,
                HttpStatus.NOT_FOUND, "No staffMember found with " + email + "!", null);
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success!",
                modelMapper.map(staffMember, StaffMemberResponseDto.class)
        );
    }

    @Override
    public ResponseDto createStaffMember(StaffMember staffMember) {
        /* check whether the email is duplicate or not in the user table **/
        User loadedUser = userService.loadUserByEmail(staffMember.getUser().getEmail());
        if (loadedUser != null) {
            return new ResponseDto(ResponseType.DUPLICATE_ENTRY, HttpStatus.CONFLICT, "Duplicate user email found!", null);
        }

        if (staffMemberRepository.existsByEmail(staffMember.getEmail())) {
            return new ResponseDto(ResponseType.DUPLICATE_ENTRY, HttpStatus.CONFLICT, "Duplicate email found!", null);
        }

        StringBuilder tempPassword = new StringBuilder();
        if (staffMember.getUser().getPassword() == null) {
            int length = 5;
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

            Random random = new Random();
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(characters.length());
                char randomChar = characters.charAt(randomIndex);
                tempPassword.append(randomChar);
            }

            staffMember.getUser().setPassword(tempPassword.toString());

            // send temp password email to staffMember
            emailSenderService.sendEmail(staffMember.getUser().getEmail(),
                    "Welcome!",
                    "Hello! " + staffMember.getUser().getFullName() + "\nYour temporary password is: " + tempPassword + "\nUse your first name as username");
        }

        User user = userService.createUser(new UserDto(staffMember.getUser().getUsername(),
                staffMember.getUser().getFullName(),
                staffMember.getUser().getEmail(), staffMember.getUser().getPassword()));



        if (staffMember.getUserType().getUserTypeId() == 2) {
            userService.assignRoleToUser(user.getUserId(), 3L);
        } else {
            userService.assignRoleToUser(user.getUserId(), 2L);
        }



//        StaffMember staffMember = staffMemberMapper.fromStaffMemberDto(staffMemberDto);
        staffMember.setUser(user);
        UserType userType = modelMapper.map(staffMember.getUserType(), UserType.class);
        staffMember.setUserType(userType);

        StaffMember savedStaffMember = staffMemberRepository.save(staffMember);

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.CREATED,
                "StaffMember has been saved successfully!",
                modelMapper.map(savedStaffMember, StaffMemberResponseDto.class)
        );
    }

    @Override
    public ResponseDto updateStaffMember(StaffMemberDto staffMemberDto) {
        StaffMember loadedStaffMember = findStaffMemberById(staffMemberDto.getStaffMemberId());
        StaffMember staffMember = modelMapper.map(staffMemberDto, StaffMember.class);

        staffMember.setUser(loadedStaffMember.getUser());
        StaffMember updatedStaffMember = staffMemberRepository.save(staffMember);
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "StaffMember has been updated successfully!",
                modelMapper.map(updatedStaffMember, StaffMemberResponseDto.class)
        );
    }

    @Override
    public ResponseDto removeStaffMember(Long staffMemberId) {
        StaffMember staffMember = findStaffMemberById(staffMemberId);
        if (staffMember == null) return new ResponseDto(ResponseType.DATA_NOT_FOUND,
                HttpStatus.NOT_FOUND, "No staffMember found with ID " + staffMemberId + "!", null);

        staffMemberRepository.deleteById(staffMemberId);

        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "StaffMember has been deleted successfully!",
                modelMapper.map(staffMember, StaffMemberResponseDto.class)
        );

    }

    @Override
    public Long getCountOfRecords() {
        return staffMemberRepository.count();
    }
}
