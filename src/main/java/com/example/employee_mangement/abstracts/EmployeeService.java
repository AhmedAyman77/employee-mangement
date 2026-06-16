package com.example.employee_mangement.abstracts;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.example.employee_mangement.dtos.CreateEmployee;
import com.example.employee_mangement.dtos.UpdateEmployee;
import com.example.employee_mangement.entities.Employee;

public interface EmployeeService {
    Employee findOne(UUID id);
    Page<Employee> findAll(int page, int size);
    Employee create(CreateEmployee employee);
    void delete(UUID id);
    Employee update(UUID id, UpdateEmployee employee);
}
