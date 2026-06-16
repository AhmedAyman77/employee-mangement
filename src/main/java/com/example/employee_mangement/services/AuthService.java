package com.example.employee_mangement.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.employee_mangement.config.JwtHelper;
import com.example.employee_mangement.dtos.LoginRequest;
import com.example.employee_mangement.dtos.ResetPasswordRequest;
import com.example.employee_mangement.dtos.SignupRequest;
import com.example.employee_mangement.entities.Employee;
import com.example.employee_mangement.entities.PasswordResetToken;
import com.example.employee_mangement.entities.UserAccount;
import com.example.employee_mangement.repositories.EmployeeRepo;
import com.example.employee_mangement.repositories.PasswordRestRepo;
import com.example.employee_mangement.repositories.UserAccountRepo;
import com.example.employee_mangement.share.CustomResponseException;

import jakarta.transaction.Transactional;


@Service
public class AuthService {
    @Autowired
    private UserAccountRepo userAccountRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordRestRepo passwordRestRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    public void signup(SignupRequest signupRequest, String token) {
        Employee employee = employeeRepo.findOneByAccountCreationToken(token)
        .orElseThrow(() -> CustomResponseException.ResourceNotFound("Invalid token"));


        if (employee.isVerified()) {
            throw CustomResponseException.BadRequest("Account already created");
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(signupRequest.username());
        userAccount.setPassword(passwordEncoder.encode(signupRequest.password()));
        userAccountRepo.save(userAccount);

        employee.setVerified(true);
        employee.setAccountCreationToken(null);
        employeeRepo.save(employee);
    }

    public String login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
            )
        );

        UserAccount userAccount = userAccountRepo.findOneByUsername(loginRequest.username())
            .orElseThrow(CustomResponseException::BadCredentials);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userAccount.getId());
        
        return jwtHelper.generateToken(claims, userAccount);
    }


    @Transactional
    public void initiatePasswordRest(String username) {
        try {
            UserAccount user = userAccountRepo.findOneByUsername(username)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Account not found"));

            String token = UUID.randomUUID().toString();
            LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setExpiryDate(expiry);
            resetToken.setUser(user);

            passwordRestRepo.save(resetToken);

            emailService.sendPasswordRestEmail(user.getEmployee().getEmail(), token);
        } catch (Exception e) {
            throw CustomResponseException.BadRequest("Sending reset password email failed");
        }
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        PasswordResetToken resetToken = passwordRestRepo.findOneByToken(resetPasswordRequest.token())
            .orElseThrow(() -> CustomResponseException.BadRequest("Invalid token"));

        boolean isTokenExpired = resetToken.getExpiryDate().isBefore(LocalDateTime.now());
        if (isTokenExpired) {
        passwordRestRepo.delete(resetToken);
        throw CustomResponseException.BadRequest("Token has expired, request a new one");
        }

        UserAccount user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        userAccountRepo.save(user);
        passwordRestRepo.delete(resetToken);
    }
}
