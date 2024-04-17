package com.helios.api.repository;

import com.helios.api.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByConsultantConsultantId(Long consultantId);

    @Query(value = "select s.scheduleId from Schedule s order by s.scheduleId desc limit 1")
    Long getLastInsertedScheduleId();

    @Query(value = "SELECT s.* FROM schedules AS s \n" +
            "WHERE s.consultant_id = :consultantId AND s.is_booked= 0\n" +
            "AND TIMESTAMP(s.start) >= ADDTIME(TIMESTAMP(NOW()), '0 1:0:0')\n" +
            "ORDER BY TIMESTAMP(s.`start`)", nativeQuery = true)
    List<Schedule> findAvailableSchedulesByConsultantId(@Param("consultantId") Long consultantId);

    @Modifying
    @Query("UPDATE Schedule s SET s.isBooked =:isBooked WHERE s.scheduleId =:scheduleId")
    void changeScheduleBookedStatus(@Param("scheduleId") Long scheduleId, @Param("isBooked") boolean isBooked);

    Long countScheduleByConsultantConsultantId(Long consultantId);
}
