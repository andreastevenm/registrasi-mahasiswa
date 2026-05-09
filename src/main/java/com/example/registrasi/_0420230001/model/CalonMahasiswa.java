package com.example.registrasi._0420230001.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "calon_mahasiswa")
public class CalonMahasiswa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nim;
    private String namaLengkap;
    private String email;
    private String nomorTelepon;
    private LocalDate tanggalLahir;
    private String programStudi;
    private String alamatLengkap;
    private String asalSekolah;
    private String status;
    private LocalDate tanggalDaftar;

    public CalonMahasiswa() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNomorTelepon() { return nomorTelepon; }
    public void setNomorTelepon(String nomorTelepon) { this.nomorTelepon = nomorTelepon; }

    public LocalDate getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(LocalDate tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getProgramStudi() { return programStudi; }
    public void setProgramStudi(String programStudi) { this.programStudi = programStudi; }

    public String getAlamatLengkap() { return alamatLengkap; }
    public void setAlamatLengkap(String alamatLengkap) { this.alamatLengkap = alamatLengkap; }

    public String getAsalSekolah() { return asalSekolah; }
    public void setAsalSekolah(String asalSekolah) { this.asalSekolah = asalSekolah; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getTanggalDaftar() { return tanggalDaftar; }
    public void setTanggalDaftar(LocalDate tanggalDaftar) { this.tanggalDaftar = tanggalDaftar; }
}