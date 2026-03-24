package com.pnc.izin.dao;

import com.pnc.izin.config.DatabaseHelper;
import com.pnc.izin.entity.PengajuanIzin;
import com.pnc.izin.entity.IzinSakit;
import com.pnc.izin.entity.IzinPenting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IzinDAO {

    /**
     * Method untuk menyimpan pengajuan izin baru ke database.
     */
    public void ajukanIzin(PengajuanIzin izin) {
        String sql = "INSERT INTO pengajuan_izin (id_mahasiswa, tanggal, durasi_hari, status, jenis_izin, ada_surat_dokter, kategori) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, izin.getIdMahasiswa());
            pstmt.setString(2, izin.getTanggal());
            pstmt.setInt(3, izin.getDurasiHari());
            pstmt.setString(4, izin.getStatus());

            if (izin instanceof IzinSakit) {
                IzinSakit sakit = (IzinSakit) izin;
                
                pstmt.setString(5, "Sakit"); 
                pstmt.setInt(6, sakit.isAdaSuratDokter() ? 1 : 0); 
                pstmt.setString(7, null);

            } else if (izin instanceof IzinPenting) {
                IzinPenting penting = (IzinPenting) izin;
                pstmt.setString(5, "Penting");
                pstmt.setNull(6, java.sql.Types.INTEGER);
                pstmt.setString(7, penting.getKategori());
            }

            pstmt.executeUpdate();
            
            System.out.println("[SUCCESS] Pengajuan Izin berhasil disimpan ke sistem!");

        } catch (SQLException e) {
            System.out.println("[ERROR] Gagal menyimpan pengajuan izin: " + e.getMessage());
        }
    }

    /**
     * Method untuk melihat riwayat izin milik satu mahasiswa.
     */
    public void tampilkanRiwayatIzin(int idMahasiswa) {
        String sql = "SELECT * FROM pengajuan_izin WHERE id_mahasiswa = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idMahasiswa);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n===== RIWAYAT PENGAJUAN IZIN =====");
                boolean adaData = false;

                while (rs.next()) {
                    adaData = true;
                    System.out.println("ID Izin     : " + rs.getInt("id"));
                    System.out.println("Tanggal     : " + rs.getString("tanggal"));
                    System.out.println("Durasi      : " + rs.getInt("durasi_hari") + " hari");
                    System.out.println("Jenis       : " + rs.getString("jenis_izin"));
                    System.out.println("Status      : " + rs.getString("status"));
                    System.out.println("------------------------------");
                }
                if (!adaData) {
                    System.out.println("Anda belum pernah mengajukan izin.");
                }
            }
        } catch (Exception e) {
            System.out.println("[DB ERROR] Gagal mengambil riwayat izin: " + e.getMessage());
        }
    }

    /**
     * Method untuk menampilkan daftar izin yang masih "Menunggu Dosen Wali"
     * khusus untuk mahasiswa yang dibimbing oleh dosen tertentu.
     */
    public void tampilkanIzinPending(int idDosenWali) {
        String sql = "SELECT p.id, u.nama, u.nim, p.tanggal, p.durasi_hari, p.jenis_izin, p.status " +
                     "FROM pengajuan_izin p " +
                     "JOIN user u ON p.id_mahasiswa = u.id " +
                     "WHERE u.id_dosen_wali = ? AND p.status = 'Menunggu Dosen Wali'";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idDosenWali);

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n===== DAFTAR PENGAJUAN IZIN (PENDING) =====");
                boolean adaData = false;

                while (rs.next()) {
                    adaData = true;                                    
                    System.out.println("ID Izin   : " + rs.getInt("id"));
                    System.out.println("Mahasiswa : " + rs.getString("nama") + " (" + rs.getString("nim") + ")");
                    System.out.println("Tanggal   : " + rs.getString("tanggal"));
                    System.out.println("Durasi    : " + rs.getInt("durasi_hari") + " hari");
                    System.out.println("Jenis Izin: " + rs.getString("jenis_izin"));
                    System.out.println("---------------------------------------");
                }

                if (!adaData) {
                    System.out.println("Tidak ada pengajuan izin baru.");
                }
            }

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengambil data izin pending: " + e.getMessage());
        }
    }

    /**
     * Method untuk meng-update status izin (Approve / Reject) di database.
     */
    public void updateStatusIzin(int idIzin, String statusBaru) {
        String sql = "UPDATE pengajuan_izin SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, statusBaru);
            pstmt.setInt(2, idIzin);

            int barisBerubah = pstmt.executeUpdate();
            
            if (barisBerubah > 0) {
                System.out.println("[DB SUCCESS] Status izin ID " + idIzin + " berhasil diubah menjadi: " + statusBaru);
            } else {
                System.out.println("[DB WARNING] ID Izin tidak ditemukan!");
            }

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengupdate status izin: " + e.getMessage());
        }
    }

    /**
     * Method untuk menampilkan daftar izin antrean Koordinator Prodi
     */
    public void tampilkanIzinPendingKoorprodi() {
        String sql = "SELECT p.id, u.nama, u.nim, p.tanggal, p.durasi_hari, p.jenis_izin, p.status " +
                     "FROM pengajuan_izin p " + 
                     "JOIN user u ON p.id_mahasiswa = u.id " +
                     "WHERE p.status = 'Menunggu Koorprodi'";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("\n===== ANTREAN IZIN KOORDINATOR PRODI =====");
            boolean adaData = false;

            while (rs.next()) {
                adaData = true;
                System.out.println("ID Izin     : " + rs.getInt("id"));
                System.out.println("Mahasiswa   : " + rs.getString("nama") + " (" + rs.getString("nim") + ")");
                System.out.println("Tanggal     : " + rs.getString("tanggal"));
                System.out.println("Durasi      : " + rs.getInt("durasi_hari") + " hari");
                System.out.println("Jenis Izin  : " + rs.getString("jenis_izin"));
                System.out.println("-----------------------------------------");
            }

            if (!adaData) {
                System.out.println("Tidak ada pengajuan izin yang menunggu Koorprodi.");
            }
        } catch (Exception e) {
            System.out.println("[DB ERROR] Gagal mengambil antrean Koorprodi: " + e.getMessage());
        }
    }

    /**
     * Method Helper untuk mengambil durasi hari dari sebuah izin
     */
    public int getDurasiIzin(int idIzin) {
        String sql = "SELECT durasi_hari FROM pengajuan_izin WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idIzin);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("durasi_hari");
                }
            }
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengecek durasi izin: " + e.getMessage());
        }
        return 0;
    }
}