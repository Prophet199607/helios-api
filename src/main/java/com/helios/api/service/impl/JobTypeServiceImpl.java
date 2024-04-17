package com.helios.api.service.impl;

import com.helios.api.dto.JobTypeDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.JobType;
import com.helios.api.repository.JobTypeRepository;
import com.helios.api.service.JobTypeService;
import com.helios.api.util.ResponseType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class JobTypeServiceImpl implements JobTypeService {
    @Autowired
    private JobTypeRepository jobTypeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public JobType loadJobTypeById(Long jobTypeId) {
        return jobTypeRepository.findById(jobTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Job Type not found!"));
    }

    @Override
    public JobType createJobType(String jobType, int status) {
        return jobTypeRepository.save(new JobType(jobType, status));
    }

    @Override
    public ResponseDto fetchJobTypes() {
        List<JobTypeDto> jobTypes = jobTypeRepository.findAll()
                .stream().map(country -> modelMapper.map(country, JobTypeDto.class))
                .toList();
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success",
                jobTypes
        );
    }
}
