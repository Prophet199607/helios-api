package com.helios.api.service;

import com.helios.api.dto.ConsultantDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.Consultant;

public interface ConsultantService {
    Consultant findConsultantById(Long consultantId);
    Consultant findConsultantByUser(Long userId);
    ResponseDto fetchConsultantById(Long consultantId);
    ResponseDto fetchConsultants();
    ResponseDto fetchAllConsultantsWithPagination(int page, int size);
    ResponseDto findConsultantsByName(String name, int page, int size);
    ResponseDto loadConsultantByEmail(String email);
    ResponseDto createConsultant(Consultant consultantDto);
    ResponseDto updateConsultant(ConsultantDto consultantDto);
    ResponseDto removeConsultant(Long consultantId);
    Long getCountOfRecords();
    Long getScheduleCount(Long consultantId);
    Long getAppointmentsCount(Long consultantId);
    Long getNewAppointmentsCount(Long consultantId);
}
