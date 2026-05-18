package com.otfiling.service;

import com.otfiling.dto.OTApplicationRequest;
import com.otfiling.entity.Employee;
import com.otfiling.entity.OTApplication;
import com.otfiling.repository.EmployeeRepository;
import com.otfiling.repository.OTApplicationRepository;
import com.otfiling.service.LateFilingService.LateFilingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class OTApplicationService {
    
    @Autowired
    private OTApplicationRepository otRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private LateFilingService lateFilingService;
    
    /**
     * Create new OT application with automatic late filing detection
     */
    @Transactional
    public OTApplication createOTApplication(OTApplicationRequest request) {
        // Fetch employee
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        // Calculate OT hours
        BigDecimal otHours = calculateOTHours(request.getTimeStart(), request.getTimeEnd());
        
        // Create application
        OTApplication application = new OTApplication();
        application.setEmployee(employee);
        application.setApplicationType(request.getApplicationType());
        application.setDateOfOvertime(request.getDateOfOvertime());
        application.setTypeOfApplication(request.getTypeOfApplication());
        application.setTimeStart(request.getTimeStart());
        application.setTimeEnd(request.getTimeEnd());
        application.setOtHours(otHours);
        application.setReasonOfFiling(request.getReasonOfFiling());
        application.setRemarks(request.getRemarks());
        application.setAlreadyAppliedInAm(request.getAlreadyAppliedInAm());
        application.setDateAppliedInAm(request.getDateAppliedInAm());
        application.setFiledDate(LocalDateTime.now());
        application.setStatus("Pending");
        
        // Check late filing
        LateFilingResult lateFilingResult = lateFilingService.checkLateFiling(
            request.getDateOfOvertime(),
            LocalDateTime.now(),
            employee.getHasShuttle()
        );
        
        application.setIsLateFiling(lateFilingResult.isLateFiling());
        application.setLateFilingReason(lateFilingResult.getReason());
        
        return otRepository.save(application);
    }
    
    /**
     * Calculate OT hours from start and end time
     */
    private BigDecimal calculateOTHours(LocalTime start, LocalTime end) {
        Duration duration = Duration.between(start, end);
        long minutes = duration.toMinutes();
        double hours = minutes / 60.0;
        return BigDecimal.valueOf(hours).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Get all OT applications
     */
    public List<OTApplication> getAllApplications() {
        return otRepository.findAll();
    }
    
    /**
     * Get OT application by ID
     */
    public OTApplication getApplicationById(Long id) {
        return otRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("OT Application not found"));
    }
    
    /**
     * Get OT applications by employee
     */
    public List<OTApplication> getApplicationsByEmployee(Long employeeId) {
        return otRepository.findByEmployeeId(employeeId);
    }
    
    /**
     * Update OT application status
     */
    @Transactional
    public OTApplication updateStatus(Long id, String status, String approvedBy) {
        OTApplication application = getApplicationById(id);
        application.setStatus(status);
        
        if ("Approved".equals(status)) {
            application.setApprovedBy(approvedBy);
            application.setApprovedDate(LocalDateTime.now());
        }
        
        return otRepository.save(application);
    }
    
    /**
     * Delete OT application
     */
    @Transactional
    public void deleteApplication(Long id) {
        otRepository.deleteById(id);
    }
    
    /**
     * Get late filed applications
     */
    public List<OTApplication> getLateFiled() {
        return otRepository.findByIsLateFiling(true);
    }
}