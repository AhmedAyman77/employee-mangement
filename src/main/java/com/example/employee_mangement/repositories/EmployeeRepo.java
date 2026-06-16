package com.example.employee_mangement.repositories;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employee_mangement.entities.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, UUID> {
    Optional<Employee> findOneByAccountCreationToken(String token);

}
