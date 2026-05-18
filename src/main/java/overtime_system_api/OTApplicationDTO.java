package com.otfiling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// Request DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTApplicationRequest {
    private Long employeeId;
    private String applicationType;
    private LocalDate dateOfOvertime;
    private String typeOfApplication;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String reasonOfFiling;
    private String remarks;
    private Boolean alreadyAppliedInAm;
    private LocalDate dateAppliedInAm;
}

// Response DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
class OTApplicationResponse {
    private Long id;
    private EmployeeDTO employee;
    private String applicationType;
    private LocalDate dateOfOvertime;
    private LocalDateTime filedDate;
    private String typeOfApplication;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private BigDecimal otHours;
    private String reasonOfFiling;
    private String remarks;
    private Boolean isLateFiling;
    private String lateFilingReason;
    private String status;
    private Boolean alreadyAppliedInAm;
    private LocalDate dateAppliedInAm;
    private String appliedBy;
    private String approvedBy;
    private LocalDateTime approvedDate;
    private LocalDateTime createdAt;
}

// Employee DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
class EmployeeDTO {
    private Long id;
    private String employeeId;
    private String fullName;
    private String groupName;
    private Boolean hasShuttle;
    private String email;
}

// Employee Auto-fill Response
@Data
@NoArgsConstructor
@AllArgsConstructor
class EmployeeAutofillResponse {
    private Long id;
    private String employeeId;
    private String fullName;
    private String groupName;
    private Boolean hasShuttle;
}

// Late Filing Check Response
@Data
@NoArgsConstructor
@AllArgsConstructor
class LateFilingCheckResponse {
    private Boolean isLateFiling;
    private String reason;
    private LocalDateTime filingDeadline;
}