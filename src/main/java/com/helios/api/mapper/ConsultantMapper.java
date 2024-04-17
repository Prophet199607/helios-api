package com.helios.api.mapper;

import com.helios.api.dto.ConsultantDto;
import com.helios.api.entity.Consultant;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ConsultantMapper {
    public ConsultantDto fromConsultant(Consultant consultant) {
        ConsultantDto consultantDto = new ConsultantDto();
        BeanUtils.copyProperties(consultant, consultantDto);
        return consultantDto;
    }

    public Consultant fromConsultantDto(ConsultantDto consultantDto) {
        Consultant consultant = new Consultant();
        BeanUtils.copyProperties(consultantDto, consultant);
        return consultant;
    }
}
