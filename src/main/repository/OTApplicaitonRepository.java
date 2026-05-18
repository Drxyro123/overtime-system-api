package com.otfiling.repository;

import com.otfiling.entity.OTApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OTApplicationRepository extends JpaRepository<OTApplication, Long> {
    
    List<OTApplication> findByEmployeeId(Long employeeId);
    
    List<OTApplication> findByDateOfOvertime(LocalDate dateOfOvertime);
    
    List<OTApplication> findByStatus(String status);
    
    List<OTApplication> findByIsLateFiling(Boolean isLateFiling);
    
    @Query("SELECT o FROM OTApplication o WHERE o.employee.id = :employeeId " +
           "AND o.dateOfOvertime BETWEEN :startDate AND :endDate")
    List<OTApplication> findByEmployeeIdAndDateRange(
        @Param("employeeId") Long employeeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT o FROM OTApplication o WHERE o.dateOfOvertime BETWEEN :startDate AND :endDate")
    List<OTApplication> findByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT o FROM OTApplication o WHERE o.employee.groupName = :groupName " +
           "AND o.dateOfOvertime = :date")
    List<OTApplication> findByGroupAndDate(
        @Param("groupName") String groupName,
        @Param("date") LocalDate date
    );
}