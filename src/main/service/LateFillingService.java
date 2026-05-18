package com.otfiling.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class LateFilingService {
    
    private static final int SALARY_RELEASE_DAY_1 = 5;
    private static final int SALARY_RELEASE_DAY_2 = 20;
    private static final int LATE_FILING_DAYS_BEFORE = 5;
    private static final LocalTime CUTOFF_TIME = LocalTime.of(11, 59, 0);
    private static final LocalTime END_OF_DAY = LocalTime.of(17, 30, 0);
    
    /**
     * Check if the OT filing is considered late based on:
     * 1. Time of filing (after 11:59 AM on same day)
     * 2. Filing within 5 days before salary release (5th and 20th)
     * 
     * @param dateOfOvertime The date when overtime will be performed
     * @param filingDateTime The date and time when the OT is being filed
     * @param hasShuttle Whether the employee has shuttle service
     * @return LateFilingResult containing status and reason
     */
    public LateFilingResult checkLateFiling(
            LocalDate dateOfOvertime, 
            LocalDateTime filingDateTime,
            Boolean hasShuttle) {
        
        LocalDate filingDate = filingDateTime.toLocalDate();
        LocalTime filingTime = filingDateTime.toLocalTime();
        
        // Rule 1: Same-day filing after 11:59 AM
        if (filingDate.equals(dateOfOvertime)) {
            if (filingTime.isAfter(CUTOFF_TIME)) {
                String reason = String.format(
                    "Filed at %s, after cutoff time of 11:59 AM on the same day",
                    filingTime.toString()
                );
                
                // Add shuttle-specific message
                if (hasShuttle != null && hasShuttle) {
                    reason += ". Employee with shuttle assignment will miss scheduled shuttle.";
                }
                
                return new LateFilingResult(true, reason);
            }
        }
        
        // Rule 2: Filing within 5 days before salary release
        int filingDay = filingDate.getDayOfMonth();
        int month = filingDate.getMonthValue();
        int year = filingDate.getYear();
        
        // Check if filing is within 5 days before 5th of month
        if (filingDay >= (SALARY_RELEASE_DAY_1 - LATE_FILING_DAYS_BEFORE + 1) && 
            filingDay <= SALARY_RELEASE_DAY_1) {
            String reason = String.format(
                "Filed on day %d, which is within 5 days before salary release on the 5th",
                filingDay
            );
            return new LateFilingResult(true, reason);
        }
        
        // Check if filing is within 5 days before 20th of month
        if (filingDay >= (SALARY_RELEASE_DAY_2 - LATE_FILING_DAYS_BEFORE + 1) && 
            filingDay <= SALARY_RELEASE_DAY_2) {
            String reason = String.format(
                "Filed on day %d, which is within 5 days before salary release on the 20th",
                filingDay
            );
            return new LateFilingResult(true, reason);
        }
        
        // Handle end of month edge case for 5th of next month
        int lastDayOfMonth = filingDate.lengthOfMonth();
        if (filingDay >= (lastDayOfMonth - (LATE_FILING_DAYS_BEFORE - SALARY_RELEASE_DAY_1))) {
            String reason = String.format(
                "Filed on day %d, which is within 5 days before salary release on the 5th of next month",
                filingDay
            );
            return new LateFilingResult(true, reason);
        }
        
        // Not late filing
        return new LateFilingResult(false, null);
    }
    
    /**
     * Calculate the next valid filing deadline for a given overtime date
     */
    public LocalDateTime getFilingDeadline(LocalDate overtimeDate) {
        return LocalDateTime.of(overtimeDate, CUTOFF_TIME);
    }
    
    /**
     * Check if current date is within blackout period for filing
     */
    public boolean isInBlackoutPeriod(LocalDate date) {
        int day = date.getDayOfMonth();
        
        // Days 1-5 (before 5th release)
        if (day >= 1 && day <= SALARY_RELEASE_DAY_1) {
            return true;
        }
        
        // Days 16-20 (before 20th release)
        if (day >= (SALARY_RELEASE_DAY_2 - LATE_FILING_DAYS_BEFORE + 1) && 
            day <= SALARY_RELEASE_DAY_2) {
            return true;
        }
        
        // Last 4 days of month (before 5th of next month)
        int lastDay = date.lengthOfMonth();
        if (day >= (lastDay - (LATE_FILING_DAYS_BEFORE - SALARY_RELEASE_DAY_1))) {
            return true;
        }
        
        return false;
    }
    
    // Inner class for result
    public static class LateFilingResult {
        private final boolean isLateFiling;
        private final String reason;
        
        public LateFilingResult(boolean isLateFiling, String reason) {
            this.isLateFiling = isLateFiling;
            this.reason = reason;
        }
        
        public boolean isLateFiling() {
            return isLateFiling;
        }
        
        public String getReason() {
            return reason;
        }
    }
}