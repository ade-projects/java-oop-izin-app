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
            System.out.println("NIM         : " + mhs.getNim());
            System.out.println("Total Alpa  : " + mhs.getTotalJamAlpa() + " jam");
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

                    System.out.print("Ada Surat Dokter? (Y/N): ");
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

                    System.out.print("Kategori (Keluarga/Pribadi/Tugas): ");
                    String kategori = scanner.nextLine();

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
            System.out.println("[0] Logout");
            System.out.print("Pilih menu: ");

            int pilihan = scanner.nextInt();
            scanner.nextLine(); 

            switch (pilihan) {
                case 1:
                    userDAO.tampilkanSemuaUser();
                    
                    break;
                case 2:
                    userDAO.tampilkanSemuaUser();
                    System.out.print("\nMasukkan ID Pengguna yang ingin dihapus (atau 0 untuk batal): ");
                    int idHapus = scanner.nextInt();
                    scanner.nextLine();
                    
                    if (idHapus != 0) {
                        System.out.print("Yakin ingin menghapus user ID " + idHapus + "? (Y/N): ");
                        if (scanner.nextLine().equalsIgnoreCase("Y")) {
                            userDAO.hapusUser(idHapus);
                        } else {
                            System.out.println("Penghapusan dibatalkan.");
                        }
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