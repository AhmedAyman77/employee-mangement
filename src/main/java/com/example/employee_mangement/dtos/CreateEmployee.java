package com.example.employee_mangement.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateEmployee(
    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    String firstName,
    
    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    String lastName,

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    String email,

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    String phoneNumber,

    @NotNull(message = "Hire date cannot be null")
    @PastOrPresent(message = "Hire date cannot be in the future")
    LocalDate hireDate,

    @NotNull(message = "Position cannot be null")
    @Size(min = 2, max = 100, message = "Position must be between 2 and 100 characters")
    String position,
    
    @NotNull(message = "department cannot be null")
    UUID departmentId
) {}
