package com.helios.api.service;

import com.helios.api.dto.StaffMemberDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.StaffMember;

public interface StaffMemberService {
    StaffMember findStaffMemberById(Long staffMemberId);
    StaffMember findStaffMemberByUser(Long userId);
    ResponseDto fetchStaffMemberById(Long staffMemberId);
    ResponseDto fetchStaffMembers();
    ResponseDto fetchAllStaffMembersWithPagination(int page, int size);
    ResponseDto findStaffMembersByName(String name, int page, int size);
    ResponseDto loadStaffMemberByEmail(String email);
    ResponseDto createStaffMember(StaffMember staffMemberDto);
    ResponseDto updateStaffMember(StaffMemberDto staffMemberDto);
    ResponseDto removeStaffMember(Long staffMemberId);
    Long getCountOfRecords();
}
