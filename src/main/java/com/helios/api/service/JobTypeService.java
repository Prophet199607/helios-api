package com.helios.api.service;

import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.JobType;

public interface JobTypeService {
    JobType loadJobTypeById(Long jobTypeId);
    JobType createJobType(String jobType, int status);
    ResponseDto fetchJobTypes();
}
