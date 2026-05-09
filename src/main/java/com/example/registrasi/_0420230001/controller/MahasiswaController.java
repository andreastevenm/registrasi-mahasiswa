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

/**
 * Controller untuk halaman welcome, pendaftaran calon mahasiswa baru,
 * dan halaman sukses.
 */
@Controller
public class MahasiswaController {

    @Autowired
    private MahasiswaService mahasiswaService;

    // ===================== WELCOME PAGE =====================

    /**
     * Halaman awal — pilih peran (Admin / Mahasiswa Baru).
     */
    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    // ===================== FORM PENDAFTARAN =====================

    /**
     * Tampilkan form pendaftaran calon mahasiswa baru.
     * Path dipindah ke /daftar-mahasiswa agar / bisa jadi welcome page.
     */
    @GetMapping("/daftar-mahasiswa")
    public String showForm(Model model) {
        model.addAttribute("mahasiswa", new CalonMahasiswa());
        model.addAttribute("tahunAkademik", "2026/2027");
        return "form-pendaftaran";
    }

    /**
     * Proses submit data pendaftaran calon mahasiswa baru.
     * Validasi server-side dilakukan di sini.
     */
    @PostMapping("/daftar")
    public String prosesPendaftaran(
            @ModelAttribute CalonMahasiswa mahasiswa,
            @RequestParam("captchaInput") String captchaInput,
            @RequestParam("captchaSession") String captchaSession,
            Model model) {

        // --- Validasi nama (minimal 4 karakter) ---
        if (mahasiswa.getNamaLengkap() == null || mahasiswa.getNamaLengkap().trim().length() < 4) {
            model.addAttribute("errorNama", "Inputan nama tidak valid, kurang dari empat karakter");
            model.addAttribute("mahasiswa", mahasiswa);
            return "form-pendaftaran";
        }

        // --- Validasi NIM (hanya angka, tepat 10 digit) ---
        if (mahasiswa.getNim() == null || !mahasiswa.getNim().matches("\\d{10}")) {
            model.addAttribute("errorNim", "Panjang NIM tidak valid, harus 10 digit angka");
            model.addAttribute("mahasiswa", mahasiswa);
            return "form-pendaftaran";
        }

        // --- Validasi email unik (mencegah double submission) ---
        if (mahasiswaService.emailSudahTerdaftar(mahasiswa.getEmail())) {
            model.addAttribute("errorEmail", "Email sudah digunakan, satu email hanya bisa mendaftar satu kali");
            model.addAttribute("mahasiswa", mahasiswa);
            return "form-pendaftaran";
        }

        // --- Validasi NIM unik ---
        if (mahasiswaService.nimSudahTerdaftar(mahasiswa.getNim())) {
            model.addAttribute("errorNim", "NIM sudah terdaftar");
            model.addAttribute("mahasiswa", mahasiswa);
            return "form-pendaftaran";
        }

        // --- Validasi nomor telepon (format 08 atau 62, 10-13 digit) ---
        String noTelp = mahasiswa.getNomorTelepon();
        if (noTelp == null || !noTelp.matches("(08|62)\\d{8,11}")) {
            model.addAttribute("errorTelepon", "Nomor telepon tidak valid. Format: 08 atau 62, panjang 10-13 digit");
            model.addAttribute("mahasiswa", mahasiswa);
            return "form-pendaftaran";
        }

        // --- Validasi umur minimal 18 tahun ---
        if (mahasiswa.getTanggalLahir() != null) {
            int umur = Period.between(mahasiswa.getTanggalLahir(), LocalDate.now()).getYears();
            if (umur < 18) {
                model.addAttribute("errorTanggalLahir", "Calon mahasiswa harus berumur minimal 18 tahun");
                model.addAttribute("mahasiswa", mahasiswa);
                return "form-pendaftaran";
            }
        }

        // --- Validasi alamat (minimal 15 karakter) ---
        if (mahasiswa.getAlamatLengkap() == null || mahasiswa.getAlamatLengkap().trim().length() < 15) {
            model.addAttribute("errorAlamat", "Alamat tidak valid, minimal 15 karakter");
            model.addAttribute("mahasiswa", mahasiswa);
            return "form-pendaftaran";
        }

        // --- Validasi captcha (case-insensitive) ---
        if (!captchaInput.equalsIgnoreCase(captchaSession)) {
            model.addAttribute("errorCaptcha", "Kode captcha tidak sesuai, silakan coba lagi");
            model.addAttribute("mahasiswa", mahasiswa);
            return "form-pendaftaran";
        }

        // --- Simpan ke database dengan status PENDING ---
        mahasiswaService.daftar(mahasiswa);

        // Redirect ke halaman sukses
        return "redirect:/sukses";
    }

    /**
     * Halaman notifikasi pendaftaran berhasil.
     */
    @GetMapping("/sukses")
    public String halamanSukses() {
        return "sukses";
    }
}