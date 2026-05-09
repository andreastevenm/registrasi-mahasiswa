package com.example.registrasi._0420230001.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.registrasi._0420230001.model.Admin;
import com.example.registrasi._0420230001.model.CalonMahasiswa;
import com.example.registrasi._0420230001.service.AdminService;
import com.example.registrasi._0420230001.service.MahasiswaService;

import jakarta.servlet.http.HttpSession;

// Controller untuk semua halaman admin: login, dashboard, pencarian, verifikasi, logout.
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MahasiswaService mahasiswaService;

    // ===================== LOGIN =====================

    // Tampilkan halaman login admin.
    @GetMapping("/login")
    public String showLogin() {
        return "admin-login";
    }

    // Proses login admin, simpan session jika berhasil.
    @PostMapping("/login")
    public String prosesLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Optional<Admin> admin = adminService.login(username, password);

        if (admin.isPresent()) {
            // Simpan info admin ke session
            session.setAttribute("adminLogin", admin.get().getUsername());
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("errorLogin", "Username atau password salah!");
            return "admin-login";
        }
    }

    // ===================== DASHBOARD =====================

    // Halaman dashboard admin dengan statistik dan daftar calon mahasiswa.
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Cek session, jika belum login redirect ke login
        if (session.getAttribute("adminLogin") == null) {
            return "redirect:/admin/login";
        }

        List<CalonMahasiswa> daftarMahasiswa = mahasiswaService.getSemuaMahasiswa();

        model.addAttribute("daftarMahasiswa", daftarMahasiswa);
        model.addAttribute("totalPendaftar", mahasiswaService.totalPendaftar());
        model.addAttribute("totalPending", mahasiswaService.totalPending());
        model.addAttribute("totalVerified", mahasiswaService.totalVerified());
        model.addAttribute("adminNama", session.getAttribute("adminLogin"));

        return "dashboard";
    }

    // ===================== PENCARIAN =====================

    // Halaman pencarian data calon mahasiswa berdasarkan nama/NIM dan status.
    @GetMapping("/pencarian")
    public String pencarian(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "SEMUA") String status,
            HttpSession session,
            Model model) {

        if (session.getAttribute("adminLogin") == null) {
            return "redirect:/admin/login";
        }

        List<CalonMahasiswa> hasil = mahasiswaService.cari(keyword, status);

        model.addAttribute("hasil", hasil);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statusFilter", status);
        model.addAttribute("jumlahHasil", hasil.size());

        return "pencarian";
    }

    // ===================== VERIFIKASI =====================

    // Tampilkan halaman verifikasi untuk calon mahasiswa tertentu.
    @GetMapping("/verifikasi/{id}")
    public String showVerifikasi(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("adminLogin") == null) {
            return "redirect:/admin/login";
        }

        Optional<CalonMahasiswa> mahasiswa = mahasiswaService.getById(id);
        if (mahasiswa.isEmpty()) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("mahasiswa", mahasiswa.get());
        return "verifikasi";
    }

    // Proses verifikasi (update status: VERIFIED / REJECTED).
    @PostMapping("/verifikasi/{id}")
    public String prosesVerifikasi(
            @PathVariable Long id,
            @RequestParam String statusVerifikasi,
            HttpSession session) {

        if (session.getAttribute("adminLogin") == null) {
            return "redirect:/admin/login";
        }

        mahasiswaService.updateStatus(id, statusVerifikasi);
        return "redirect:/admin/dashboard";
    }

    // ===================== LOGOUT =====================

    // Proses logout admin, hapus session.
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}