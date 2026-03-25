# Tugas Pemrograman Berorientasi Objek: Sistem Pengajuan Izin PNC

Proyek ini adalah implementasi sistem informasi manajemen pengajuan izin ketidakhadiran mahasiswa berbasis terminal (CLI). Aplikasi ini dikembangkan menggunakan bahasa pemrograman Java dengan penerapan prinsip **Pemrograman Berorientasi Objek (OOP)** secara mendalam, serta menggunakan database SQLite untuk persistensi data.

## Identitas Mahasiswa

- **Nama**: Ade Ariansyah Anggoro
- **NIM**: 250315034
- **Kelas**: TRPL 1B
- **Mata Kuliah**: Pemrograman Berorientasi Objek
- **Institusi**: Politeknik Negeri Cilacap (PNC)

---

## Deskripsi Proyek

Sistem Pengajuan Izin PNC dirancang untuk mendigitalisasi alur birokrasi perizinan mahasiswa. Sistem ini menerapkan arsitektur _Clean Code_ dengan pemisahan antara _Business Logic_ (OOP), UI (Terminal), dan _Database Layer_ (DAO). Aplikasi ini juga mengusung fitur **Multi-Tier Approval Workflow**, di mana izin diproses secara berjenjang dari Dosen Wali hingga Koordinator Program Studi.

### Fitur Utama:

1. **Multi-Role Access**: Sistem membedakan hak akses dan antarmuka untuk 3 jenis peran: **Mahasiswa**, **Dosen**, dan **Admin**.
2. **Pengajuan Izin Pintar**: Mahasiswa dapat mengajukan Izin Sakit (dengan validasi surat dokter) dan Izin Penting (dengan dropdown kategori anti-typo).
3. **Multi-Tier Approval**:
   - Izin $\le$ 1 hari: Disetujui langsung oleh Dosen Wali.
   - Izin $>$ 1 hari: Melewati Dosen Wali, lalu diteruskan ke Koordinator Prodi untuk persetujuan final.
4. **CRUD Pengguna (Admin Mode)**: Admin memiliki wewenang penuh untuk Menambah, Menampilkan, Mengubah (termasuk jam alpa), dan Menghapus data pengguna dari sistem.
5. **Kompensasi Otomatis**: Sistem menggunakan konsep OOP untuk menghitung total sanksi kompensasi mahasiswa berdasarkan akumulasi jam alpa.
6. **Persistensi Data**: Seluruh data riwayat izin dan profil pengguna tersimpan aman di dalam file `data_izin.db`.

---

## Teknologi yang Digunakan

- **Bahasa**: Java (JDK)
- **Build Tool**: Apache Maven (dengan `maven-assembly-plugin` untuk _Fat JAR_)
- **Database**: SQLite
- **Library**: SQLite JDBC Driver

---

## Struktur Kode & Arsitektur OOP

Aplikasi ini dibagi menjadi beberapa _layer_ untuk menerapkan prinsip _Single Responsibility Principle_ dan konsep OOP lanjutan (Inheritance, Polymorphism, Encapsulation):

- **Layer Entity / Model (`com.pnc.izin.entity`)**:
  - `User` (Induk) $\rightarrow$ Diturunkan menjadi `Mahasiswa`, `Dosen`, dan `Admin`.
  - `PengajuanIzin` (Induk) $\rightarrow$ Diturunkan menjadi `IzinSakit` dan `IzinPenting`. Di sini tersimpan aturan validasi batas hari.
- **Layer DAO (`com.pnc.izin.dao`)**:
  - Berisi `UserDAO` dan `IzinDAO` yang bertugas mengeksekusi _query_ ke database (Menerapkan _PreparedStatement_ untuk mencegah _SQL Injection_).
- **Layer Config (`com.pnc.izin.config`)**:
  - `DatabaseHelper.java` menangani koneksi SQLite dan auto-inisialisasi tabel.
- **Main App (`com.pnc.izin.Main`)**:
  - Menangani interaksi _Scanner_, antarmuka terminal, dan perutean menu.

---

## Tutorial Menjalankan Proyek

### 1. Prasyarat

Pastikan sistem Anda sudah terinstal:

- Java JDK (Minimal versi 8, direkomendasikan versi terbaru).
- Apache Maven.

### 2. Kompilasi dan Build (_Fat JAR_)

Buka terminal di folder root proyek (sejajar dengan file `pom.xml`), lalu jalankan perintah:

```bash
mvn clean package
```

Perintah ini akan mengunduh dependensi SQLite dan membungkus seluruh aplikasi menjadi satu file JAR mandiri (_Uber JAR_) di dalam folder `target/`.

### 3. Menjalankan Aplikasi

Gunakan perintah berikut untuk menjalankan program secara langsung:

```bash
java -jar target/uts-pbo-1.0-SNAPSHOT-jar-with-dependencies.jar
```

_(Catatan: Sesuaikan nama file JAR dengan versi yang dihasilkan di folder target Anda)_

Atau, Anda dapat menjalankannya langsung melalui Maven:

```bash
mvn exec:java -Dexec.mainClass="com.pnc.izin.Main"
```

---

## Skema Database Utama

Aplikasi secara otomatis membuat dua tabel utama yang saling berelasi:

1. **Tabel `user`**: Menyimpan identitas mahasiswa, dosen, dan admin (termasuk kolom spesifik seperti `id_dosen_wali`, `total_jam_alpa`, `is_koor_prodi`).
2. **Tabel `pengajuan_izin`**: Menyimpan detail transaksi izin dengan referensi _Foreign Key_ ke tabel `user`.

---
