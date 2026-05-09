package com.example.registrasi._0420230001.repository;

import com.example.registrasi._0420230001.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository untuk akses data Admin ke database.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Cari admin berdasarkan username dan password untuk login
    Optional<Admin> findByUsernameAndPassword(String username, String password);
}