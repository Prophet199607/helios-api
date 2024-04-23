package com.helios.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Basic
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Basic
    @Column(name = "email", nullable = false)
    private String email;

    @Basic
    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Basic
    @Column(name = "nic", nullable = false)
    private String nic;

    @Basic
    @Column(name = "address1", nullable = false)
    private String address1;

    @Basic
    @Column(name = "address2", nullable = false)
    private String address2;

    @Basic
    @Column(name = "birthday", nullable = true)
    private String birthday;

    @Basic
    @Column(name = "gender", nullable = true)
    private String gender;

    @Basic
    @Column(name = "remark", nullable = true)
    private String remark;

    @Basic
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

//    @OneToMany(mappedBy = "jobSeeker", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private Set<Appointment> appointments = new HashSet<>();

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;
}
