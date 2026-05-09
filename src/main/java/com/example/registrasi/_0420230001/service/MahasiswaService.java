package com.example.registrasi._0420230001.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.registrasi._0420230001.model.CalonMahasiswa;
import com.example.registrasi._0420230001.repository.CalonMahasiswaRepository;

/**
 * Service layer untuk logika bisnis pendaftaran calon mahasiswa.
 */
@Service
public class MahasiswaService {

    @Autowired
    private CalonMahasiswaRepository repository;

    /**
     * Simpan data calon mahasiswa baru ke database.
     * Status awal selalu PENDING.
     */
    public void daftar(CalonMahasiswa mahasiswa) {
        mahasiswa.setStatus("PENDING");
        mahasiswa.setTanggalDaftar(LocalDate.now());
        repository.save(mahasiswa);
    }

    /**
     * Cek apakah email sudah pernah digunakan untuk mendaftar.
     */
    public boolean emailSudahTerdaftar(String email) {
        return repository.existsByEmail(email);
    }

    /**
     * Cek apakah NIM sudah pernah digunakan.
     */
    public boolean nimSudahTerdaftar(String nim) {
        return repository.existsByNim(nim);
    }

    /**
     * Ambil semua data calon mahasiswa (untuk dashboard admin).
     */
    public List<CalonMahasiswa> getSemuaMahasiswa() {
        return repository.findAll();
    }

    /**
     * Pencarian berdasarkan keyword (NIM atau Nama) dan status.
     */
    public List<CalonMahasiswa> cari(String keyword, String status) {
        return repository.cariByKeywordDanStatus(keyword, status);
    }

    /**
     * Ambil data mahasiswa berdasarkan ID (untuk halaman verifikasi).
     */
    public Optional<CalonMahasiswa> getById(Long id) {
        return repository.findById(id);
    }

    /**
     * Update status verifikasi mahasiswa (VERIFIED / REJECTED).
     */
    public void updateStatus(Long id, String status) {
        Optional<CalonMahasiswa> opt = repository.findById(id);
        opt.ifPresent(m -> {
            m.setStatus(status);
            repository.save(m);
        });
    }

    /**
     * Hitung total seluruh pendaftar.
     */
    public long totalPendaftar() {
        return repository.count();
    }

    /**
     * Hitung jumlah yang masih menunggu verifikasi (PENDING).
     */
    public long totalPending() {
        return repository.countByStatus("PENDING");
    }

    /**
     * Hitung jumlah yang sudah terverifikasi (VERIFIED).
     */
    public long totalVerified() {
        return repository.countByStatus("VERIFIED");
    }
}