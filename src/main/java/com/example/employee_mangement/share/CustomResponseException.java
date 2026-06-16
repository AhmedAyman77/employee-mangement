package com.example.employee_mangement.share;
import org.springframework.security.authentication.BadCredentialsException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomResponseException extends RuntimeException {
    private final int status;
    private final String message;

    public static CustomResponseException ResourceNotFound(String message) {
        return new CustomResponseException(404, message);
    }

    public static CustomResponseException BadCredentials() {
        return new CustomResponseException(401, "Bad credentials");
    }
    
    public static CustomResponseException BadRequest(String message) {
        return new CustomResponseException(400, message);
    }

}
