package com.example.registrasi._0420230001.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.registrasi._0420230001.model.Admin;
import com.example.registrasi._0420230001.repository.AdminRepository;

/**
 * Service layer untuk logika autentikasi admin.
 */
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Validasi login admin berdasarkan username dan password.
     * Mengembalikan Optional<Admin> jika cocok.
     */
    public Optional<Admin> login(String username, String password) {
        return adminRepository.findByUsernameAndPassword(username, password);
    }
}