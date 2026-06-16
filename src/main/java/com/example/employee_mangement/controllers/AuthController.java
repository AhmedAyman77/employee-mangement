package com.example.employee_mangement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_mangement.dtos.LoginRequest;
import com.example.employee_mangement.dtos.ResetPasswordRequest;
import com.example.employee_mangement.dtos.SignupRequest;
import com.example.employee_mangement.services.AuthService;
import com.example.employee_mangement.share.GlobalResponse;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<String>> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);

        return new ResponseEntity<>(
            new GlobalResponse<>(token), HttpStatus.CREATED);
    }


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest, @RequestParam String token) {
        authService.signup(signupRequest, token);
        return ResponseEntity.ok("User registered successfully");
    }
    
    @PostMapping("/forgot-password/{username}")
    public ResponseEntity<GlobalResponse<String>> forgotPassword(@PathVariable String username) {

        authService.initiatePasswordRest(username);
        return new ResponseEntity<>(new GlobalResponse<>("Password reset email sent!"), HttpStatus.CREATED);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<GlobalResponse<String>> resetPassword(
        @RequestBody ResetPasswordRequest resetPasswordRequest
    ) {
        authService.resetPassword(resetPasswordRequest);
        return new ResponseEntity<>(new GlobalResponse<>("Password reset successfully!"), HttpStatus.CREATED);
    }
}
