package com.helios.api.repository;

import com.helios.api.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientPatientId(Long PatientId);

    List<Appointment> findAppointmentsByPatientPatientId(@Param("PatientId") Long PatientId);

    @Query(value = "select a from Appointment as a where a.consultant.consultantId=:consultantId and a.status =:status")
    List<Appointment> findAppointmentsByConsultantIdAndStatus(@Param("consultantId") Long consultantId, @Param("status") int status);

    @Modifying
    @Query(value = "UPDATE Appointment as a SET a.status =:status, a.isAccepted =:isAccepted WHERE a.appointmentId =:appointmentId")
    void changeAppointmentStatus(@Param("appointmentId") Long appointmentId,
                                 @Param("status") int status,
                                 @Param("isAccepted") boolean isAccepted);

    @Query(value = "SELECT * FROM appointments as a WHERE DATE(a.create_date) \n" +
            "BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Appointment> getAllAppointmentsByDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

    Long countAppointmentByConsultantConsultantId(Long consultantId);

    Long countAppointmentByConsultantConsultantIdAndStatus(Long consultantId, int status);
}
