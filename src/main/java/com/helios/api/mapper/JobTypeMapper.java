package com.helios.api.mapper;

import com.helios.api.dto.JobTypeDto;
import com.helios.api.entity.JobType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class JobTypeMapper {
    public JobTypeDto fromJobType(JobType jobType) {
        JobTypeDto jobTypeDto = new JobTypeDto();
        BeanUtils.copyProperties(jobType, jobTypeDto);
        return jobTypeDto;
    }

    public JobType fromJobTypeDto(JobTypeDto jobTypeDto) {
        JobType jobType = new JobType();
        BeanUtils.copyProperties(jobTypeDto, jobType);
        return jobType;
    }
}
