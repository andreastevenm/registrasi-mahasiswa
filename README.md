# Sistem Registrasi Calon Mahasiswa Baru – Dregister

Aplikasi web berbasis Spring Boot untuk pendaftaran dan verifikasi calon mahasiswa baru Tahun Akademik 2026/2027.

## Teknologi yang Digunakan

- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- Thymeleaf
- MySQL (SQLyog)
- Maven

## Cara Menjalankan Aplikasi

### Prasyarat

- Java 21 sudah terinstal
- MySQL Server berjalan (SQLyog atau XAMPP)
- Maven terinstal (atau gunakan Maven Wrapper `./mvnw`)

### Langkah Setup

1. **Clone repository**

```bash
   git clone https://github.com/username/registrasi-mahasiswa.git
   cd registrasi-mahasiswa
```

2. **Buat database**
   Buka SQLyog atau MySQL client, jalankan:

```sql
   CREATE DATABASE db_registrasi;
```

3. **Sesuaikan konfigurasi database**
   Edit file `src/main/resources/application.properties`:

```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/db_registrasi
   spring.datasource.username=root
   spring.datasource.password=password_anda
```

4. **Jalankan aplikasi**

```bash
   ./mvnw spring-boot:run
```

Atau jika Maven sudah terinstal global:

```bash
   mvn spring-boot:run
```

5. **Buka browser**
   Akses: [http://localhost:8080](http://localhost:8080)

## Fitur Aplikasi

### Halaman Mahasiswa

| URL                 | Keterangan                           |
| ------------------- | ------------------------------------ |
| `/`                 | Halaman selamat datang – pilih peran |
| `/daftar-mahasiswa` | Form pendaftaran calon mahasiswa     |
| `/sukses`           | Konfirmasi pendaftaran berhasil      |

### Halaman Admin

| URL                      | Keterangan                     |
| ------------------------ | ------------------------------ |
| `/admin/login`           | Login admin                    |
| `/admin/dashboard`       | Daftar & statistik pendaftar   |
| `/admin/pencarian`       | Cari data berdasarkan nama/NIM |
| `/admin/verifikasi/{id}` | Verifikasi / tolak pendaftar   |
| `/admin/logout`          | Logout admin                   |

## Akun Admin Default

Tambahkan data admin langsung ke database:

```sql
INSERT INTO admins (username, password) VALUES ('admin', 'admin123');
```

## Struktur Folder

```
src/
├── main/
│   ├── java/com/example/registrasi/
│   │   ├── controller/   # AdminController, MahasiswaController
│   │   ├── model/        # Admin, CalonMahasiswa
│   │   ├── repository/   # AdminRepository, CalonMahasiswaRepository
│   │   └── service/      # AdminService, MahasiswaService
│   └── resources/
│       ├── templates/    # HTML Thymeleaf
│       ├── static/css/   # style.css
│       └── application.properties
└── test/
```

"PENTING UNTUK DIBACA"
Bila mengalami "Whitelabel Error" saat pendaftaran form, harap kembali kepada website sebelumnya dengan cara
klik tanda panah balik (<-) pada kiri atas website lalu pastikan semua section sudah terisi dan setelah itu
lakukan submit kembali.

Troubleshooting

- Aplikasi tidak bisa start / error koneksi database
- Pastikan MySQL Server sedang berjalan
- Cek username dan password di application.properties sudah benar
- Pastikan database db_registrasi sudah dibuat

Tidak bisa login admin

- Pastikan sudah menjalankan perintah INSERT INTO admins ... setelah aplikasi pertama kali jalan
- Cek data admin di MySQL: SELECT \* FROM admins;

"Whitelabel Error" saat submit form

- Klik tombol back (←) di browser
- Pastikan semua field sudah terisi dengan benar
- Coba submit kembali
