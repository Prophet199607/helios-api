package com.helios.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_types")
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usertype_id")
    private Long userTypeId;

    @Basic
    @Column(name = "user_type")
    private String userType;

    @Basic
    @Column(name = "status")
    private int status;

    @OneToMany(mappedBy = "userType", fetch = FetchType.LAZY)
    private Set<StaffMember> staffMembers = new HashSet<>();

    public UserType(Long userTypeId) {
        this.userTypeId = userTypeId;
    }

    public UserType(String userType, int status) {
        this.userType = userType;
        this.status = status;
    }

    public UserType(Long userTypeId, String userType, int status) {
        this.userTypeId = userTypeId;
        this.userType = userType;
        this.status = status;
    }
}
