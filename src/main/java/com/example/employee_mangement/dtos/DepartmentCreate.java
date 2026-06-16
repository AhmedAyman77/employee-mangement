package com.example.employee_mangement.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartmentCreate(
    @NotNull(message = "name cannot be null")
    @Size(min = 2, max = 50, message = "name must be between 2 and 50 characters")
    String name
) {

}
