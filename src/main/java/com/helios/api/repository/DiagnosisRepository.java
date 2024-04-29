package com.helios.api.repository;

import com.helios.api.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    List<Diagnosis> findDiagnosesByPatientPatientId(@Param("PatientId") Long patientId);
}
