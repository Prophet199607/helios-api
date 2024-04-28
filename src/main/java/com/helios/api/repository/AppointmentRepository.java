package com.helios.api.repository;

import com.helios.api.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAppointmentsByPatientPatientId(@Param("PatientId") Long PatientId);

    @Query(value = "select a from Appointment as a inner join Patient " +
            "as p on a.patient.patientId = p.patientId where p.nic=:nic and a.status =:status")
    List<Appointment> findAppointmentsByPatientNicAndStatus(@Param("nic") String nic, @Param("status") int status);

    @Modifying
    @Query(value = "UPDATE Appointment as a SET a.status =:status WHERE a.appointmentId =:appointmentId")
    void changeAppointmentStatus(@Param("appointmentId") Long appointmentId,
                                 @Param("status") int status);
}
