package com.otfiling.repository;

import com.otfiling.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeId(String employeeId);
    Optional<Employee> findByFullName(String fullName);
    List<Employee> findByFullNameContainingIgnoreCase(String fullName);
    List<Employee> findByGroupName(String groupName);
}