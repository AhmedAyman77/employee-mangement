package com.example.employee_mangement.abstracts;

import java.util.List;
import java.util.UUID;

import com.example.employee_mangement.dtos.DepartmentCreate;
import com.example.employee_mangement.entities.Department;

public interface DepartmentService {
    Department findOne(UUID id);
    List<Department> findAll();
    Department create(DepartmentCreate department);
    void delete(UUID id);
    // Department update(UUID id, Department department);
}
