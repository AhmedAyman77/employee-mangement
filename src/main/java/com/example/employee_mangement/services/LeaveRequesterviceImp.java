package com.example.employee_mangement.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.employee_mangement.abstracts.LeaveRequestervice;
import com.example.employee_mangement.dtos.LeaveRequestCreate;
import com.example.employee_mangement.entities.Employee;
import com.example.employee_mangement.entities.LeaveRequest;
import com.example.employee_mangement.repositories.EmployeeRepo;
import com.example.employee_mangement.repositories.LeaveRequestRepo;
import com.example.employee_mangement.share.CustomResponseException;

@Service
public class LeaveRequesterviceImp implements LeaveRequestervice {
    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private LeaveRequestRepo leaveRequestRepo;



    @Override
    @PreAuthorize("@securityUtils.isOwner(#id)")
    public LeaveRequest create(LeaveRequestCreate leaveRequest, UUID employeeId) {
        LeaveRequest newLeaveRequest = new LeaveRequest();

        newLeaveRequest.setReason(leaveRequest.reason());
        newLeaveRequest.setStatus("Pending");
        newLeaveRequest.setStartDate(leaveRequest.startDate());
        newLeaveRequest.setEndDate(leaveRequest.endDate());

        Employee employee = employeeRepo.findById(employeeId)
            .orElseThrow(() -> CustomResponseException.resourceNotFound("Employee not found with id: " + employeeId));
        
        newLeaveRequest.setEmployee(employee);

        return leaveRequestRepo.save(newLeaveRequest);
    }

    @Override
    @PreAuthorize("@securityUtils.isOwner(#id)")
    public List<LeaveRequest> findAllByEmployeeId(UUID employeeId) {
        employeeRepo.findById(employeeId)
            .orElseThrow(() -> CustomResponseException.resourceNotFound("Employee not found with id: " + employeeId));

        return leaveRequestRepo.findAllByEmployeeId(employeeId);
    }

}
