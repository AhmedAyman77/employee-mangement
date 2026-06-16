package com.example.employee_mangement.abstracts;

import java.util.List;
import java.util.UUID;

import com.example.employee_mangement.dtos.LeaveRequestCreate;
import com.example.employee_mangement.entities.LeaveRequest;

public interface LeaveRequestervice {
    LeaveRequest create(LeaveRequestCreate leaveRequest, UUID employeeId);
    List<LeaveRequest> findAllByEmployeeId(UUID employeeId);
}
