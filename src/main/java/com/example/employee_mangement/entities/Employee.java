package com.example.employee_mangement.entities;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false, length = 100)
    private String position;

    @Column(name="is_verified", columnDefinition="BOOLEAN DEFAULT FALSE", nullable = false)
    private Boolean isVerified;

    @Column(name="account_creation_token")
    private String accountCreationToken;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonProperty("departmentId") // This will make sure that the JSON property is named "departmentId" instead of "department"
    private Department departmentId;

    public UUID getDepartmentId() {
        return departmentId.getId();
    }
}
