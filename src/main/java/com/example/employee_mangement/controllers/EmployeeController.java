package com.example.employee_mangement.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_mangement.abstracts.EmployeeService;
import com.example.employee_mangement.abstracts.LeaveRequestervice;
import com.example.employee_mangement.dtos.CreateEmployee;
import com.example.employee_mangement.dtos.LeaveRequestCreate;
import com.example.employee_mangement.dtos.PaginatedResponse;
import com.example.employee_mangement.dtos.UpdateEmployee;
import com.example.employee_mangement.entities.Employee;
import com.example.employee_mangement.entities.LeaveRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LeaveRequestervice leaveRequestService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<Employee>> findAll(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "10") int size,
    HttpServletRequest request) {

        int zeroBasedPage = page - 1;
        Page<Employee> employees = employeeService.findAll(zeroBasedPage, size);

        String baseUrl = request.getRequestURL().toString();
        String nextUrl = employees.hasNext() ? String.format("%s?page=%d&size=%d", baseUrl, page + 1, size) : null;
        String prevUrl = employees.hasPrevious() ? String.format("%s?page=%d&size=%d", baseUrl, page - 1, size) : null;

        var paginatedResponse = new PaginatedResponse<Employee>(
            employees.getContent(),
            employees.getNumber(),
            employees.getTotalPages(),
            employees.getTotalElements(),
            employees.hasNext(),
            employees.hasPrevious(),
            nextUrl,
            prevUrl
        );

        return ResponseEntity.status(200).body(paginatedResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable UUID id) {
        Employee employee = employeeService.findOne(id);
        return employee != null ? ResponseEntity.status(200).body(employee) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody CreateEmployee employee) {
        Employee createdEmployee = employeeService.create(employee);
        return ResponseEntity.status(201).body(createdEmployee);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@Valid @PathVariable UUID id, @RequestBody UpdateEmployee employee) {
        Employee updatedEmployee = employeeService.update(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PostMapping("/{employeeId}/leave-requests")
    public ResponseEntity<LeaveRequest> createLeaveRequest(@PathVariable UUID employeeId, @Valid @RequestBody LeaveRequestCreate leaveRequest) {
        LeaveRequest createdLeaveRequest = leaveRequestService.create(leaveRequest, employeeId);
        return ResponseEntity.status(201).body(createdLeaveRequest);
    }

    @GetMapping("/{employeeId}/leave-requests")
    public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByEmployeeId(@PathVariable UUID employeeId) {
        List<LeaveRequest> leaveRequests = leaveRequestService.findAllByEmployeeId(employeeId);
        return ResponseEntity.ok(leaveRequests);
    }
}
