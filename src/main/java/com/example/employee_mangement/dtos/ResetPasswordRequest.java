package com.example.employee_mangement.dtos;

public record ResetPasswordRequest(
    String token,
    String newPassword
) {

}
