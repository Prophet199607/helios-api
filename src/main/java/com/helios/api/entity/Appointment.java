package com.helios.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @Basic
    @Column(name = "is_accepted", nullable = false)
    private Boolean isAccepted;

    @Basic
    @Column(name = "preferred_date", nullable = false)
    private Date preferredDate;

    @Basic
    @Column(name = "preferred_time", nullable = false)
    private Time preferredTime;

    @Basic
    @Column(name = "status", nullable = false)
    private Integer status;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;
}
