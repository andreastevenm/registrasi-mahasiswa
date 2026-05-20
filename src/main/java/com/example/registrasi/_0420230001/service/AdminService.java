package com.example.registrasi._0420230001.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.registrasi._0420230001.model.Admin;

@Service
public class AdminService {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    public Optional<Admin> login(String username, String password) {
        if (DEFAULT_USERNAME.equals(username) && DEFAULT_PASSWORD.equals(password)) {
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword(password);
            return Optional.of(admin);
        }
        return Optional.empty();
    }
}