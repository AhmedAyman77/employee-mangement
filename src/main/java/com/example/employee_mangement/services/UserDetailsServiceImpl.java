package com.example.employee_mangement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.employee_mangement.entities.UserAccount;
import com.example.employee_mangement.repositories.UserAccountRepo;
import com.example.employee_mangement.share.CustomResponseException;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserAccountRepo userAccountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> account = userAccountRepo.findOneByUsername(username);
        if (account.isEmpty()) {
            throw CustomResponseException.BadCredentials();
        }
        UserAccount user = account.get();


        return User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole())
            .build();
    }
}
