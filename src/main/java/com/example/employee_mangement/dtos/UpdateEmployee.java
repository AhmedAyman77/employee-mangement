package com.example.employee_mangement.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateEmployee(
    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    String firstName,
    
    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    String lastName,

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    String phoneNumber,

    @NotNull(message = "Position cannot be null")
    @Size(min = 2, max = 100, message = "Position must be between 2 and 100 characters")
    String position
) {}
