package com.helios.api.repository;

import com.helios.api.entity.StaffMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffMemberRepository extends JpaRepository<StaffMember, Long> {
    Boolean existsByEmail(@Param("email") String email);
    StaffMember findStaffMembersByEmail(String email);
    @Query(value = "select c from StaffMember as c where c.firstName like %:name% or c.lastName like %:name%")
    Page<StaffMember> findStaffMembersByFirstName(@Param("name") String name, PageRequest pageRequest);
    @Query(value = "select c from StaffMember c")
    Page<StaffMember> getAllStaffMembersWithPagination(PageRequest pageRequest);
    @Query(value = "select c.* from staff_members as c where c.user_id=:userId", nativeQuery = true)
    StaffMember findStaffMemberByUserId(@Param("userId") Long userId);
}
