package com.helios.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "job_types")
public class JobType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jobtype_id")
    private Long jobTypeId;

    @Basic
    @Column(name = "job_type")
    private String jobType;

    @Basic
    @Column(name = "status")
    private int status;

    @OneToMany(mappedBy = "jobType", fetch = FetchType.LAZY)
    private Set<Consultant> consultants = new HashSet<>();

    public JobType(Long jobTypeId) {
        this.jobTypeId = jobTypeId;
    }

    public JobType(String jobType, int status) {
        this.jobType = jobType;
        this.status = status;
    }

    public JobType(Long jobTypeId, String jobType, int status) {
        this.jobTypeId = jobTypeId;
        this.jobType = jobType;
        this.status = status;
    }
}
