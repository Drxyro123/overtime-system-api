package com.otfiling.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "ot_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(name = "application_type", nullable = false, length = 50)
    private String applicationType;
    
    @Column(name = "date_of_overtime", nullable = false)
    private LocalDate dateOfOvertime;
    
    @Column(name = "filed_date", nullable = false)
    private LocalDateTime filedDate;
    
    @Column(name = "type_of_application", length = 50)
    private String typeOfApplication = "Regular";
    
    @Column(name = "time_start", nullable = false)
    private LocalTime timeStart;
    
    @Column(name = "time_end", nullable = false)
    private LocalTime timeEnd;
    
    @Column(name = "ot_hours", nullable = false, precision = 4, scale = 2)
    private BigDecimal otHours;
    
    @Column(name = "reason_of_filing", nullable = false, columnDefinition = "TEXT")
    private String reasonOfFiling;
    
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    @Column(name = "is_late_filing")
    private Boolean isLateFiling = false;
    
    @Column(name = "late_filing_reason", length = 255)
    private String lateFilingReason;
    
    @Column(name = "status", length = 50)
    private String status = "Pending";
    
    @Column(name = "already_applied_in_am")
    private Boolean alreadyAppliedInAm = false;
    
    @Column(name = "date_applied_in_am")
    private LocalDate dateAppliedInAm;
    
    @Column(name = "applied_by", length = 200)
    private String appliedBy;
    
    @Column(name = "approved_by", length = 200)
    private String approvedBy;
    
    @Column(name = "approved_date")
    private LocalDateTime approvedDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (filedDate == null) {
            filedDate = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}