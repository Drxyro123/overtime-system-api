package com.otfiling.controller;

import com.otfiling.entity.Employee;
import com.otfiling.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    /**
     * Get all employees
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return ResponseEntity.ok(employees);
    }
    
    /**
     * Get employee by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Auto-fill employee data by name
     * This endpoint returns employee details when user selects a name
     */
    @GetMapping("/autofill")
    public ResponseEntity<Map<String, Object>> getEmployeeAutofill(@RequestParam String name) {
        return employeeRepository.findByFullName(name)
            .map(employee -> {
                Map<String, Object> response = new HashMap<>();
                response.put("id", employee.getId());
                response.put("employeeId", employee.getEmployeeId());
                response.put("fullName", employee.getFullName());
                response.put("groupName", employee.getGroupName());
                response.put("hasShuttle", employee.getHasShuttle());
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Search employees by name (for autocomplete)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam String query) {
        List<Employee> employees = employeeRepository.findByFullNameContainingIgnoreCase(query);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * Create new employee
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }
    
    /**
     * Update employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id, 
            @RequestBody Employee employeeDetails) {
        
        return employeeRepository.findById(id)
            .map(employee -> {
                employee.setFirstName(employeeDetails.getFirstName());
                employee.setLastName(employeeDetails.getLastName());
                employee.setFullName(employeeDetails.getFullName());
                employee.setGroupName(employeeDetails.getGroupName());
                employee.setHasShuttle(employeeDetails.getHasShuttle());
                employee.setEmail(employeeDetails.getEmail());
                return ResponseEntity.ok(employeeRepository.save(employee));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Delete employee
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}