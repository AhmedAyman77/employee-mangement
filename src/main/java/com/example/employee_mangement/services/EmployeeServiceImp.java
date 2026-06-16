package com.example.employee_mangement.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.employee_mangement.abstracts.EmployeeService;
import com.example.employee_mangement.dtos.CreateEmployee;
import com.example.employee_mangement.dtos.UpdateEmployee;
import com.example.employee_mangement.entities.Employee;
import com.example.employee_mangement.repositories.EmployeeRepo;
import com.example.employee_mangement.share.CustomResponseException;
import com.example.employee_mangement.utils.SecurityUtils;
import com.example.employee_mangement.repositories.DepartmentRepo;
import com.example.employee_mangement.entities.Department;
import com.example.employee_mangement.services.EmailService;

import jakarta.transaction.Transactional;

@Service
public class EmployeeServiceImp implements EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private EmailService emailService;

    @Override
    @PreAuthorize("@securityUtils.isOwner(#id)")
    public Employee findOne(UUID id) {
        return employeeRepo.findById(id).orElse(null);
    }

    @Override
    public Page<Employee> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepo.findAll(pageable);
    }

    @Override
    @Transactional
    public Employee create(CreateEmployee employee) {
        Employee newEmployee = new Employee();
        Department department = departmentRepo.findById(employee.departmentId()).orElseThrow(() -> CustomResponseException.resourceNotFound(
            "Department with id " + employee.departmentId() + " not found"
        ));


        String token = UUID.randomUUID().toString();

        try{
            newEmployee.setVerified(false);
            newEmployee.setAccountCreationToken(token);
            
            newEmployee.setFirstName(employee.firstName());
            newEmployee.setLastName(employee.lastName());
            newEmployee.setEmail(employee.email());
            newEmployee.setPhoneNumber(employee.phoneNumber());
            newEmployee.setHireDate(employee.hireDate());
            newEmployee.setPosition(employee.position());
            newEmployee.setDepartmentId(department.getId());

            employeeRepo.save(newEmployee);
            
            emailService.sendAccountCreationEmail(newEmployee.getEmail(), token);

            return newEmployee;
        } catch (Exception e) {
            throw CustomResponseException.ResourceNotFound("Failed to create employee");
        }
    }

    @Override
    public void delete(UUID id) {
        Employee existingEmployee = employeeRepo.findById(id).orElseThrow(() -> CustomResponseException.resourceNotFound(
            "User with id " + id + " not found"
        ));

        employeeRepo.deleteById(id);
    }

    @Override
    @PreAuthorize("@securityUtils.isOwner(#id)")
    public Employee update(UUID id, UpdateEmployee employee) {
        Employee existingEmployee = employeeRepo.findById(id).orElseThrow(() -> CustomResponseException.resourceNotFound(
            "User with id " + id + " not found"
        ));

        existingEmployee.setFirstName(employee.firstName());
        existingEmployee.setLastName(employee.lastName());
        existingEmployee.setPhoneNumber(employee.phoneNumber());
        existingEmployee.setPosition(employee.position());

        employeeRepo.save(existingEmployee);

        return existingEmployee;
    }
}
