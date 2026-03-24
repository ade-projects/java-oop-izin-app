package com.pnc.izin;

import com.pnc.izin.config.DatabaseHelper;
import com.pnc.izin.dao.UserDAO;
import com.pnc.izin.dao.IzinDAO;
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
                    System.out.println("\n[INFO] Fitur Admin sedang dibangun...");
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
            System.out.println("\n=== DASHBOARD MAHASISWA ===");
            System.out.println("Selamat datang, " + mhs.getNama() + "!");
            System.out.println("NIM         : " + mhs.getNim());
            System.out.println("Total Alpa  : " + mhs.getTotalJamAlpa() + " jam");
            System.out.println("---------------------------");
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
            System.out.println("NIP         : " + dosen.getNip());
            System.out.println("---------------------------");
            System.out.println("[1]. Lihat & Proses Pengajuan Izin Mahasiswa");
            System.out.println("[0]. Logout");
            System.out.print("Pilih menu: ");

            int opsi = scanner.nextInt();
            scanner.nextLine();

            switch (opsi) {
                case 1:
                    izinDAO.tampilkanIzinPending(dosen.getId());

                    System.out.print("\nApakah Anda ingin memproses izin sekarang? (Y/N): ");
                    String proses = scanner.nextLine();

                    if (proses.equalsIgnoreCase("Y")) {
                        if (dosen.approveIzin()) {
                            System.out.print("Masukkan ID izin: ");
                            int idIzin = scanner.nextInt();
                            scanner.nextLine();
    
                            System.out.print("Masukkan Status Baru (Disetujui / Ditolak): ");
                            String statusBaru = scanner.nextLine();
    
                            izinDAO.updateStatusIzin(idIzin, statusBaru);
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
}