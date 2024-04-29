package com.helios.api.repository;

import com.helios.api.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByNic(String nic);
    Patient findPatientByEmail(String email);
    @Query(value = "select j from Patient as j where j.firstName like %:name% or j.lastName like %:name%")
    Page<Patient> findPatientsByFirstName(@Param("name") String name, PageRequest pageRequest);
    @Query(value = "select j from Patient as j")
    Page<Patient> getAllPatientsWithPagination(PageRequest pageRequest);

    @Query(value = "select j.* from patients as j where j.user_id=:userId", nativeQuery = true)
    Patient findPatientByUserId(@Param("userId") Long userId);
}
