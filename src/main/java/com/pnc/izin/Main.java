package com.pnc.izin;

import com.pnc.izin.config.DatabaseHelper;
import com.pnc.izin.dao.UserDAO;
import com.pnc.izin.dao.IzinDAO;
import com.pnc.izin.entity.Admin;
import com.pnc.izin.entity.Dosen;
import com.pnc.izin.entity.IzinPenting;
import com.pnc.izin.entity.IzinSakit;
import com.pnc.izin.entity.Mahasiswa;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserDAO userDAO = new UserDAO();
    private static IzinDAO izinDAO = new IzinDAO();

    public static void main(String[] args) {
        // Setup Database 
        DatabaseHelper.setupTabel();

        boolean isRunning = true;

        // 2. Looping Main Menu
        while (isRunning) {
            System.out.println("\n===== SISTEM PENGAJUAN IZIN PNC =====");
            System.out.println("[1] Login Mahasiswa");
            System.out.println("[2] Login Dosen");
            System.out.println("[3] Login Admin");
            System.out.println("[0] Keluar");
            System.out.print("Pilih menu: ");

            int opsi = scanner.nextInt();
            scanner.nextLine(); 

            switch (opsi) {
                case 1:
                    menuLoginMahasiswa();
                    break;
                case 2:
                    menuLoginDosen();
                    break;
                case 3:
                    menuLoginAdmin();
                    break;
                case 0:
                    System.out.println("\nTerima kasih telah menggunakan sistem ini. Sampai jumpa!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("\n[ERROR] Pilihan tidak valid, silakan coba lagi!");
            }
        }
    }

    /**
     * Method untuk menangani alur login Mahasiswa
     */
    private static void menuLoginMahasiswa() {
        // tampil user demo
        userDAO.tampilkanUserDemo("Mahasiswa");
        
        System.out.print("\nMasukkan NIM Anda: ");
        String nimInput = scanner.nextLine();

        System.out.println("Mencari data di database...");
        
        Mahasiswa mhs = userDAO.getMahasiswaByNim(nimInput);

        if (mhs != null) {
            System.out.println("Login Berhasil!");
            dashboardMahasiswa(mhs);
        } else {
            System.out.println("[ERROR] NIM tidak terdaftar!");
        }
    }

    /**
     * Method untuk Dashboard Mahasiswa setelah berhasil login
     */
    private static void dashboardMahasiswa(Mahasiswa mhs) {
        boolean isDashboardRunning = true;

        while (isDashboardRunning) {
            System.out.println("\n===== DASHBOARD MAHASISWA =====");
            System.out.println("Selamat datang, " + mhs.getNama() + "!");
            mhs.tampilkanProfil();
            System.out.println("-------------------------------");
            System.out.println("[1] Ajukan Izin Sakit");
            System.out.println("[2] Ajukan Izin Penting");
            System.out.println("[3] Lihat Riwayat Izin");
            System.out.println("[0] Logout");
            System.out.print("Pilih menu: ");

            int opsi = scanner.nextInt();
            scanner.nextLine();
        
            switch (opsi) {
                case 1:
                    System.out.println("\n----- FORM IZIN SAKIT -----");
                    System.out.print("Tanggal (DD-MM-YYYY)   : ");
                    String tglSakit = scanner.nextLine();

                    System.out.print("Durasi (Hari)          : ");
                    int durasiSakit = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Ada Surat Dokter? (Y/n): ");
                    String jawabDokter = scanner.nextLine();
                    boolean adaSurat = jawabDokter.equalsIgnoreCase("Y");

                    IzinSakit izinSakit = new IzinSakit(0, mhs.getId(), tglSakit, durasiSakit, "Menunggu Dosen Wali", adaSurat);

                    if (izinSakit.validasiDokumen()) {
                        izinDAO.ajukanIzin(izinSakit);
                    }
                    break;
                
                case 2:
                    System.out.println("\n----- FORM IZIN PENTING -----");
                    System.out.print("Tanggal (DD-MM-YYYY)  : ");
                    String tglPenting = scanner.nextLine();

                    System.out.print("Durasi (Hari)         : ");
                    int durasiPenting = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Pilih Kategori Izin:");
                    System.out.println("[1] Pribadi");
                    System.out.println("[2] Keluarga");
                    System.out.println("[3] Tugas / Dispensasi");
                    System.out.print("Pilihan (1-3): ");

                    int pilKategori = scanner.nextInt();
                    scanner.nextLine();

                    String kategori = "";
                    switch (pilKategori) {
                        case 1:
                            kategori = "Pribadi";
                            break;
                        case 2:
                            kategori = "Keluarga";
                            break;
                        case 3:
                            kategori = "Tugas/Dispensasi";
                            break;
                        default:
                            kategori = "Lainnya";
                            break;
                    }

                    IzinPenting izinPenting = new IzinPenting(0, mhs.getId(), tglPenting, durasiPenting, "Menunggu Dosen Wali", kategori);

                    if (izinPenting.validasiBatasHari()) {
                        izinDAO.ajukanIzin(izinPenting);
                    }
                    break;

                case 3:
                    izinDAO.tampilkanRiwayatIzin(mhs.getId());
                    break;
                case 0:
                    System.out.println("Logout berhasil. Kembali ke menu utama...");
                    isDashboardRunning = false;
                    break;

                default:
                    System.out.println("[ERROR] Pilihan tidak valid!");
            }
        }
    }

    /**
     * Method untuk menangani alur login Dosen
     */
    private static void menuLoginDosen() {
        // tampil user demo
        userDAO.tampilkanUserDemo("Dosen");
        
        System.out.print("\nMasukkan NIP Anda: ");
        String nipInput = scanner.nextLine();

        System.out.println("Mencari data dosen di database...");

        Dosen dosen = userDAO.getDosenByNip(nipInput);

        if (dosen != null) {
            System.out.println("Login Berhasil!");
            dashboardDosen(dosen);
        } else {
            System.out.println("[ERROR] NIP tidak terdaftar!");
        }
    }

    /**
     * Dashboard Dosen setelah berhasil login
     */
    private static void dashboardDosen(Dosen dosen) {
        boolean isDashboardRunning = true;

        while (isDashboardRunning) {
            System.out.println("\n===== DASHBOARD DOSEN =====");
            System.out.println("Selamat datang, Bapak/Ibu " + dosen.getNama() + "!");
            dosen.tampilkanProfil();
            System.out.println("---------------------------");

            if (dosen.isDosenWali()) {
                System.out.println("[1] Proses Antrean Izin Dosen Wali");
            }
            if (dosen.isKoorProdi()) {
                System.out.println("[2] Proses Antrean Izin Koordinator Prodi");
            }
            System.out.println("[0] Logout");
            System.out.print("Pilih menu: ");

            int opsi = scanner.nextInt();
            scanner.nextLine();

            switch (opsi) {
                case 1:
                    if (!dosen.isDosenWali()) {
                        System.out.println("[ERROR] Anda bukan Dosen Wali!");
                        break;
                    }

                    izinDAO.tampilkanIzinPending(dosen.getId());
                    System.out.print("\nApakah Anda ingin memproses izin sekarang? (Y/n): ");
                    String proses = scanner.nextLine();

                    if (proses.equalsIgnoreCase("Y")) {
                        System.out.print("Masukkan ID izin: ");
                        int idIzin = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Setujui izin ini? (Y/n): ");
                        boolean isDisetujui = scanner.nextLine().equalsIgnoreCase("Y");

                        if (!isDisetujui) {
                            izinDAO.updateStatusIzin(idIzin, "Ditolak");
                        } else {
                            int durasi = izinDAO.getDurasiIzin(idIzin);
                            String statusBaru = dosen.tentukanStatusDariWali(durasi);

                            izinDAO.updateStatusIzin(idIzin, statusBaru);
                        }
                    }
                    break;
                
                case 2:
                    if (!dosen.isKoorProdi()) {
                        System.out.println("[ERROR] Anda bukan Koordinator Prodi!");
                        break;
                    }

                    izinDAO.tampilkanIzinPendingKoorprodi();
                    System.out.print("\nApakah Anda ingin memproses izin sekarang? (Y/n): ");
                    if (scanner.nextLine().equalsIgnoreCase("Y")) {
                        System.out.print("Masukkan ID izin: ");
                        int idIzin = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Setujui izin ini secara final? (Y/n): ");
                        if (scanner.nextLine().equalsIgnoreCase("Y")) {
                            izinDAO.updateStatusIzin(idIzin, "Disetujui");
                        } else {
                            izinDAO.updateStatusIzin(idIzin, "Ditolak");
                        }
                    }
                    break;

                case 0:
                    System.out.println("Logout berhasil. Kembali ke menu utama...");
                    isDashboardRunning = false;
                    break;
                    
                default:
                    System.out.println("[ERROR] Pilihan tidak valid!");
            }
        }
    }

    /**
     * Method untuk menangani alur login Admin
     */
    private static void menuLoginAdmin() {
        // tampil user demo
        userDAO.tampilkanUserDemo("Admin");
        
        System.out.print("\nMasukkan NIP Admin: ");
        String nipInput = scanner.nextLine();
        System.out.println("Mencari data admin di database...");

        Admin admin = userDAO.getAdminByNip(nipInput);

        if (admin != null) {
            System.out.println("Login Berhasil!");
            dashboardAdmin(admin);
        } else {
            System.out.println("[ERROR] Kredensial Admin tidak valid!");
        }
    }

    /**
     * Method tampil daftar user
     */
    private static void tampilkanTabelUser() {
        System.out.println("\n======================= DAFTAR SELURUH PENGGUNA SISTEM =======================");
        System.out.printf("%-5s | %-30s | %-15s | %-15s\n", "ID", "NAMA", "ROLE", "IDENTITAS (NIM/NIP)");
        System.out.println("------------------------------------------------------------------------------");

        ArrayList<String> daftarUser = userDAO.getSemuaUserList();
        if (daftarUser.isEmpty()) {
            System.out.println("Belum ada data pengguna di dalam sistem.");
        } else {
            for (String userText : daftarUser) {
                System.out.println(userText);
            }
        }
        System.out.println("------------------------------------------------------------------------------");

    }

    /**
     * Dashboard Admin setelah berhasil login
     */
    private static void dashboardAdmin(Admin admin) {
        boolean isDashboardRunning = true;

        while (isDashboardRunning) {
            System.out.println("\n===== DASHBOARD ADMIN =====");
            System.out.println("Login sebagai : " + admin.getNama());
            System.out.println("---------------------------");
            System.out.println("[1] Lihat Semua Pengguna");
            System.out.println("[2] Hapus Pengguna");
            System.out.println("[3] Tambah Pengguna");
            System.out.println("[4] Update Pengguna");
            System.out.println("[0] Logout");
            System.out.print("Pilih menu: ");

            int pilihan = scanner.nextInt();
            scanner.nextLine(); 

            switch (pilihan) {
                case 1:
                    tampilkanTabelUser();
                    break;
                case 2:
                    tampilkanTabelUser();

                    System.out.print("\nMasukkan ID Pengguna yang ingin dihapus (atau 0 untuk batal): ");
                    int idHapus = scanner.nextInt();
                    scanner.nextLine();
                    
                    if (idHapus != 0) {
                        System.out.print("Yakin ingin menghapus user ID " + idHapus + "? (Y/n): ");
                        if (scanner.nextLine().equalsIgnoreCase("Y")) {
                            userDAO.hapusUser(idHapus);
                        } else {
                            System.out.println("Penghapusan dibatalkan.");
                        }
                    }
                    break;
                case 3:
                    System.out.println("\n---- TAMBAH PENGGUNA BARU -----");
                    System.out.println("Pilih role:\n[1] Mahasiswa\n[2] Dosen\n[3] Admin ");
                    System.out.print("Pilihan: ");
                    int roleBaru = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Nama Lengkap: ");
                    String namaBaru = scanner.nextLine();

                    if (roleBaru == 1) {
                        System.out.print("NIM: ");
                        String nimBaru = scanner.nextLine();
                        System.out.print("ID Dosen Wali: ");
                        int idWali = scanner.nextInt();
                        scanner.nextLine();
                        Mahasiswa mhsBaru = new Mahasiswa(0, namaBaru, "Mahasiswa", nimBaru, idWali, 0);
                        userDAO.tambahMahasiswa(mhsBaru);
                    } else if (roleBaru == 2) {
                        System.out.print("NIP: ");
                        String nipBaru = scanner.nextLine();
                        System.out.print("Apakah Dosen Wali? (Y/n): ");
                        boolean isWali = scanner.nextLine().equalsIgnoreCase("Y");
                        System.out.print("Apakah Koordinator Prodi? (Y/n): ");
                        boolean isKoor = scanner.nextLine().equalsIgnoreCase("Y");
                        Dosen dosenBaru = new Dosen(0, namaBaru, "Dosen", nipBaru, isWali, isKoor);
                        userDAO.tambahDosen(dosenBaru);
                    } else if (roleBaru == 3) {
                        System.out.print("NIP Admin: ");
                        String nipAdmin = scanner.nextLine();
                        Admin adminBaru = new Admin(0, namaBaru, "Admin", nipAdmin);
                        userDAO.tambahAdmin(adminBaru);
                    }
                    break;
                case 4:
                    System.out.println("\n----- UPDATE DATA PENGGUNA -----");

                    tampilkanTabelUser();

                    System.out.print("\nMasukkan ID Pengguna yang ingin diupdate (0 untuk batal): ");
                    int idUpdate = scanner.nextInt();
                    scanner.nextLine();

                    if (idUpdate == 0) break; 
                    String targetRole = userDAO.getRoleById(idUpdate);
                    if (targetRole == null) {
                        System.out.println("[ERROR] ID tidak ditemukan!");
                        break;
                    }

                    System.out.println("Mengedit user dengan role: " + targetRole);
                    System.out.println("(Biarkan kosong / tekan enter jika tidak ingin mengubah data teks)");

                    if (targetRole.equals("Mahasiswa")) {
                        Mahasiswa m = userDAO.getMahasiswaById(idUpdate);

                        System.out.print("Nama baru [" + m.getNama() + "]: ");
                        String uNama = scanner.nextLine();
                        if (!uNama.isEmpty()) m.setNama(uNama);

                        System.out.print("NIM baru [" + m.getNim() + "]: ");
                        String uNim = scanner.nextLine();
                        if (!uNim.isEmpty()) m.setNim(uNim);

                        System.out.print("Jam Alpa baru [Saat ini: " + m.getTotalJamAlpa() + "]: ");

                        String uAlpaStr = scanner.nextLine();
                        if (!uAlpaStr.isEmpty()) {
                            try {
                                int alpaBaru = Integer.parseInt(uAlpaStr);
                                m.setTotalJamAlpa(alpaBaru);
                            } catch (NumberFormatException e) {
                                System.out.println("[ERROR] Input Jam Alpa harus berupa angka! Perubahan alpa dibatalkan.");
                            }
                        }

                        userDAO.updateMahasiswa(m);

                        Mahasiswa mhs = userDAO.getMahasiswaByNim(m.getNim());

                        System.out.println("\n----- EVALUASI DATA MAHASISWA -----");
                        mhs.tampilkanProfil();
                    } else if (targetRole.equals("Dosen")) {
                        Dosen d = userDAO.getDosenById(idUpdate);

                        System.out.print("Nama baru [" + d.getNama() + "]: ");
                        String uNamaDosen = scanner.nextLine();
                        if (!uNamaDosen.isEmpty()) d.setNama(uNamaDosen);

                        System.out.print("NIP baru [" + d.getNip() + "]: ");
                        String uNipDosen = scanner.nextLine();
                        if (!uNipDosen.isEmpty()) d.setNip(uNipDosen);

                        userDAO.updateDosen(d);
                    } else if (targetRole.equals("Admin")) {
                        Admin a = userDAO.getAdminById(idUpdate);
                        
                        System.out.print("Nama baru [" + a.getNama() + "]: ");
                        String uNamaAdmin = scanner.nextLine();
                        if (!uNamaAdmin.isEmpty()) a.setNama(uNamaAdmin);
                        
                        System.out.print("NIP baru [" + a.getNip() + "]: ");
                        String uNipAdmin = scanner.nextLine();
                        if (!uNipAdmin.isEmpty()) a.setNip(uNipAdmin);
                        
                        userDAO.updateAdmin(a);
                    }
                    break;
                case 0:
                    isDashboardRunning = false;
                    break;
                default:
                    System.out.println("[ERROR] Pilihan tidak valid!");
            }
        }
    }
}