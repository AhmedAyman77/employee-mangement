package com.example.employee_mangement.entities;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "leave_requests")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequest {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;
    
    @Column(columnDefinition="TEXT")
    private String reason;

    @Column(nullable = false, length = 50)
    private String status; // e.g., "Pending", "Approved", "Rejected"

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public UUID getEmployee() {
        return employee.getId();
    }
}
