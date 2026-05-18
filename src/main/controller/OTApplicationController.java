package com.otfiling.controller;

import com.otfiling.dto.OTApplicationRequest;
import com.otfiling.entity.OTApplication;
import com.otfiling.service.LateFilingService;
import com.otfiling.service.OTApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ot-applications")
@CrossOrigin(origins = "http://localhost:3000")
public class OTApplicationController {
    
    @Autowired
    private OTApplicationService otService;
    
    @Autowired
    private LateFilingService lateFilingService;
    
    /**
     * Create new OT application
     */
    @PostMapping
    public ResponseEntity<OTApplication> createApplication(
            @RequestBody OTApplicationRequest request) {
        try {
            OTApplication application = otService.createOTApplication(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(application);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get all OT applications
     */
    @GetMapping
    public ResponseEntity<List<OTApplication>> getAllApplications() {
        List<OTApplication> applications = otService.getAllApplications();
        return ResponseEntity.ok(applications);
    }
    
    /**
     * Get OT application by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OTApplication> getApplicationById(@PathVariable Long id) {
        try {
            OTApplication application = otService.getApplicationById(id);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get OT applications by employee
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<OTApplication>> getApplicationsByEmployee(
            @PathVariable Long employeeId) {
        List<OTApplication> applications = otService.getApplicationsByEmployee(employeeId);
        return ResponseEntity.ok(applications);
    }
    
    /**
     * Get late filed applications
     */
    @GetMapping("/late-filed")
    public ResponseEntity<List<OTApplication>> getLateFiled() {
        List<OTApplication> applications = otService.getLateFiled();
        return ResponseEntity.ok(applications);
    }
    
    /**
     * Check if filing would be late
     */
    @GetMapping("/check-late-filing")
    public ResponseEntity<Map<String, Object>> checkLateFiling(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate overtimeDate,
            @RequestParam(required = false) Boolean hasShuttle) {
        
        LateFilingService.LateFilingResult result = lateFilingService.checkLateFiling(
            overtimeDate,
            LocalDateTime.now(),
            hasShuttle != null ? hasShuttle : false
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("isLateFiling", result.isLateFiling());
        response.put("reason", result.getReason());
        response.put("filingDeadline", lateFilingService.getFilingDeadline(overtimeDate));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Check if current date is in blackout period
     */
    @GetMapping("/blackout-check")
    public ResponseEntity<Map<String, Object>> checkBlackoutPeriod() {
        LocalDate today = LocalDate.now();
        boolean inBlackout = lateFilingService.isInBlackoutPeriod(today);
        
        Map<String, Object> response = new HashMap<>();
        response.put("isBlackoutPeriod", inBlackout);
        response.put("currentDate", today);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update application status (Approve/Reject)
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OTApplication> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            String status = statusUpdate.get("status");
            String approvedBy = statusUpdate.get("approvedBy");
            
            OTApplication updated = otService.updateStatus(id, status, approvedBy);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete OT application
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        try {
            otService.deleteApplication(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}