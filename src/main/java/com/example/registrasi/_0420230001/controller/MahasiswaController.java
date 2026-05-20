package com.example.registrasi._0420230001.controller;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.registrasi._0420230001.model.CalonMahasiswa;
import com.example.registrasi._0420230001.service.MahasiswaService;

@Controller
public class MahasiswaController {

    @Autowired
    private MahasiswaService mahasiswaService;

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/daftar-mahasiswa")
    public String showForm(Model model) {
        model.addAttribute("mahasiswa", new CalonMahasiswa());
        model.addAttribute("tahunAkademik", "2026/2027");
        return "form-pendaftaran";
    }

    @PostMapping("/daftar")
    public String prosesPendaftaran(
            @ModelAttribute CalonMahasiswa mahasiswa,
            @RequestParam("captchaInput") String captchaInput,
            @RequestParam("captchaSession") String captchaSession,
            Model model) {

        boolean hasError = false;

        // --- Validasi nama ---
        if (mahasiswa.getNamaLengkap() == null || mahasiswa.getNamaLengkap().trim().length() < 4) {
            model.addAttribute("errorNama", "Inputan nama tidak valid, kurang dari empat karakter");
            hasError = true;
        }

        // --- Validasi NIM ---
        if (mahasiswa.getNim() == null || !mahasiswa.getNim().matches("\\d{10}")) {
            model.addAttribute("errorNim", "Panjang NIM tidak valid, harus 10 digit angka");
            hasError = true;
        } else if (mahasiswaService.nimSudahTerdaftar(mahasiswa.getNim())) {
            model.addAttribute("errorNim", "NIM sudah terdaftar");
            hasError = true;
        }

        // --- Validasi email ---
        if (mahasiswaService.emailSudahTerdaftar(mahasiswa.getEmail())) {
            model.addAttribute("errorEmail", "Email sudah digunakan, satu email hanya bisa mendaftar satu kali");
            hasError = true;
        }

        // --- Validasi telepon ---
        String noTelp = mahasiswa.getNomorTelepon();
        if (noTelp == null || !noTelp.matches("(08|62)\\d{8,11}")) {
            model.addAttribute("errorTelepon", "Nomor telepon tidak valid. Format: 08 atau 62, panjang 10-13 digit");
            hasError = true;
        }

        // --- Validasi umur (tanggal lahir TIDAK direset) ---
        if (mahasiswa.getTanggalLahir() != null) {
            int umur = Period.between(mahasiswa.getTanggalLahir(), LocalDate.now()).getYears();
            if (umur < 18) {
                model.addAttribute("errorTanggalLahir", "Calon mahasiswa harus berumur minimal 18 tahun");
                hasError = true;
            }
        } else {
            model.addAttribute("errorTanggalLahir", "Tanggal lahir wajib diisi");
            hasError = true;
        }

        // --- Validasi alamat ---
        if (mahasiswa.getAlamatLengkap() == null || mahasiswa.getAlamatLengkap().trim().length() < 15) {
            model.addAttribute("errorAlamat", "Alamat tidak valid, minimal 15 karakter");
            hasError = true;
        }

        // --- Validasi captcha ---
        if (!captchaInput.equalsIgnoreCase(captchaSession)) {
            model.addAttribute("errorCaptcha", "Kode captcha tidak sesuai, silakan coba lagi");
            hasError = true;
        }

        // Jika ada error, kembalikan form dengan data yang sudah diisi (tanggal lahir tetap)
        if (hasError) {
            model.addAttribute("mahasiswa", mahasiswa);
            return "form-pendaftaran";
        }

        mahasiswaService.daftar(mahasiswa);
        return "redirect:/sukses";
    }

    @GetMapping("/sukses")
    public String halamanSukses() {
        return "sukses";
    }
}