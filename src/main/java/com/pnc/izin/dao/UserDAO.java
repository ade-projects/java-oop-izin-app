package com.pnc.izin.dao;

import com.pnc.izin.config.DatabaseHelper;
import com.pnc.izin.entity.Dosen;
import com.pnc.izin.entity.Mahasiswa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Method untuk menyimpan data Mahasiswa baru ke tabel 'user'
     */
    public void tambahMahasiswa(Mahasiswa mhs) {
        String sql = "INSERT INTO user (nama, role, nim, id_dosen_wali, total_jam_alpa) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mhs.getNama());
            pstmt.setString(2, mhs.getRole());
            pstmt.setString(3, mhs.getNim());
            pstmt.setInt(4, mhs.getIdDosenWali());
            pstmt.setInt(5, mhs.getTotalJamAlpa());
            
            pstmt.executeUpdate();
            System.out.println("[DB SUCCESS] Data Mahasiswa berhasil disimpan ke database!");

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal menyimpan data mahasiswa: " + e.getMessage());
        }
    }

    /**
     * Method untuk mencari Mahasiswa berdasarkan NIM
     */
    public Mahasiswa getMahasiswaByNim(String nimInput) {
        String sql = "SELECT * FROM user WHERE role = 'Mahasiswa' AND nim = ?";
        Mahasiswa mhs = null;

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nimInput);

            // Mengeksekusi query pencarian dan menampung hasilnya di ResultSet
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nama = rs.getString("nama");
                    String role = rs.getString("role");
                    String nim = rs.getString("nim");
                    int idDosenWali = rs.getInt("id_dosen_wali");
                    int jamAlpa = rs.getInt("total_jam_alpa");
                    mhs = new Mahasiswa(id, nama, role, nim, idDosenWali, jamAlpa);
                }
            }

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengambil data mahasiswa: " + e.getMessage());
        }

        return mhs;
    }

    /**
     * Method khusus untuk menyuntikkan data Dosen ke database.
     * Kita buat parameter ID manual agar bisa disamakan dengan id_dosen_wali mahasiswa.
     */
    public void tambahDosen(Dosen dosen) {
        // Perhatikan kita memasukkan 'id' secara manual di sini
        String sql = "INSERT INTO user (nama, role, nip, is_dosen_wali, is_koor_prodi) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dosen.getNama());
            pstmt.setString(2, dosen.getRole());
            pstmt.setString(3, dosen.getNip());
            pstmt.setInt(4, dosen.isDosenWali() ? 1 : 0);
            pstmt.setInt(5, dosen.isKoorProdi() ? 1 : 0);
            
            pstmt.executeUpdate();
            System.out.println("[DB SUCCESS] Data Dosen (" + dosen.getNama() + ") berhasil disimpan!");

        } catch (SQLException e) {
                System.out.println("[DB ERROR] Gagal menyimpan data dosen: " + e.getMessage());
            
        }
    }

    /**
     * Method untuk mencari Dosen berdasarkan NIP (Untuk Login Dosen)
     */
    public Dosen getDosenByNip(String nipInput) {
        String sql = "SELECT * FROM user WHERE role = 'Dosen' AND nip = ?";
        Dosen dosen = null;

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nipInput);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nama = rs.getString("nama");
                    String role = rs.getString("role");
                    String nip = rs.getString("nip");
                    boolean isDosenWali = rs.getInt("is_dosen_wali") == 1;
                    boolean isKoorProdi = rs.getInt("is_koor_prodi") == 1;
                    
                    dosen = new Dosen(id, nama, role, nip, isDosenWali, isKoorProdi);
                }
            }

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengambil data dosen: " + e.getMessage());
        }
        return dosen;
    }
}