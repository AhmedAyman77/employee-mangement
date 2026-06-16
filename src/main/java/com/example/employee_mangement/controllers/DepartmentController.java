package com.example.employee_mangement.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_mangement.abstracts.DepartmentService;
import com.example.employee_mangement.dtos.DepartmentCreate;
import com.example.employee_mangement.entities.Department;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> findAll() {
        return ResponseEntity.status(200).body(departmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> findById(@PathVariable UUID id) {
        Department department = departmentService.findOne(id);
        return department != null ? ResponseEntity.status(200).body(department) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Department> create(@Valid @RequestBody DepartmentCreate department) {
        Department createdDepartment = departmentService.create(department);

        return ResponseEntity.status(201).body(createdDepartment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
