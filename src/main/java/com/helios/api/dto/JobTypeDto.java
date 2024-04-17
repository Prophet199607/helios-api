package com.helios.api.dto;

import lombok.Data;

@Data
public class JobTypeDto {
    private Long jobTypeId;
    private String jobType;
    private int status;
}
