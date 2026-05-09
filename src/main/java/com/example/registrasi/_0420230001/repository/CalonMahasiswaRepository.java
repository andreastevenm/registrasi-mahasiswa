package com.example.registrasi._0420230001.repository;

import com.example.registrasi._0420230001.model.CalonMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository untuk akses data CalonMahasiswa ke database.
 */
@Repository
public interface CalonMahasiswaRepository extends JpaRepository<CalonMahasiswa, Long> {

    // Cek apakah email sudah terdaftar (mencegah double submission)
    boolean existsByEmail(String email);

    // Cek apakah NIM sudah terdaftar
    boolean existsByNim(String nim);

    // Cari berdasarkan NIM atau Nama (untuk fitur pencarian admin)
    @Query("SELECT c FROM CalonMahasiswa c WHERE " +
           "(LOWER(c.nim) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.namaLengkap) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status = 'SEMUA' OR c.status = :status)")
    List<CalonMahasiswa> cariByKeywordDanStatus(
        @Param("keyword") String keyword,
        @Param("status") String status
    );

    // Hitung jumlah per status
    long countByStatus(String status);

    // Cari berdasarkan ID
    Optional<CalonMahasiswa> findById(Long id);
}