package com.pnc.izin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.pnc.izin.config.DatabaseHelper;
import com.pnc.izin.entity.IzinPenting;
import com.pnc.izin.entity.IzinSakit;
import com.pnc.izin.entity.PengajuanIzin;

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
     * Method untuk mengambil riwayat izin dan mengembalikannya dalam bentuk ArrayList
     */
    public ArrayList<PengajuanIzin> getRiwayatIzin(int idMahasiswa) {
        ArrayList<PengajuanIzin> daftarIzin = new ArrayList<>();
        String sql = "SELECT * FROM pengajuan_izin WHERE id_mahasiswa = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idMahasiswa);
            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String tanggal = rs.getString("tanggal");
                    int durasi = rs.getInt("durasi_hari");
                    String status = rs.getString("status");
                    String jenis = rs.getString("jenis_izin");

                    if (jenis.equals("Sakit")) {
                        boolean adaSurat = rs.getInt("ada_surat_dokter") == 1;
                        IzinSakit sakit = new IzinSakit(id, idMahasiswa, tanggal, durasi, status, adaSurat);
                        daftarIzin.add(sakit);
                    } else if (jenis.equals("Penting")){
                        String kategori = rs.getString("kategori");
                        IzinPenting penting = new IzinPenting(id, idMahasiswa, tanggal, durasi, status, kategori);
                        daftarIzin.add(penting);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[DB ERROR] Gagal mengambil riwayat izin: " + e.getMessage());
        }
        return daftarIzin;
    }

    /**
     * Mengambil daftar izin pending untuk Dosen Wali dan memasukkan data nama mahasiswa
     */
    public ArrayList<PengajuanIzin> getIzinPendingWali(int idDosenWali) {
        ArrayList<PengajuanIzin> daftarIzin = new ArrayList<>();
       String sql = "SELECT p.*, u.nama, u.nim " +
                    "FROM pengajuan_izin p " +
                    "JOIN user u ON p.id_mahasiswa = u.id " +
                    "WHERE u.id_dosen_wali = ? AND p.status = 'Menunggu Dosen Wali'";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idDosenWali);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {

                    int id = rs.getInt("id");
                    int idMahasiswa = rs.getInt("id_mahasiswa");
                    String tanggal = rs.getString("tanggal");
                    int durasi = rs.getInt("durasi_hari");
                    String status = rs.getString("status");
                    String jenis = rs.getString("jenis_izin");

                    PengajuanIzin izinObj = null;

                    if (jenis.equals("Sakit")) {
                        boolean adaSurat = rs.getInt("ada_surat_dokter") == 1;
                        izinObj = new IzinSakit(id, idMahasiswa, tanggal, durasi, status, adaSurat);
                    } else if (jenis.equals("Penting")){
                        String kategori = rs.getString("kategori");
                        izinObj = new IzinPenting(id, idMahasiswa, tanggal, durasi, status, kategori);
                    }

                    if (izinObj != null) {
                        izinObj.setNamaMahasiswa(rs.getString("nama"));
                        izinObj.setNimMahasiswa(rs.getString("nim"));

                        daftarIzin.add(izinObj);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengambil data izin pending: " + e.getMessage());
        }
        return daftarIzin;
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
    public ArrayList<PengajuanIzin> getIzinPendingKoorprodi() {
        ArrayList<PengajuanIzin> daftarIzin = new ArrayList<>();
        
        String sql = "SELECT p.*, u.nama, u.nim " +
                     "FROM pengajuan_izin p " + 
                     "JOIN user u ON p.id_mahasiswa = u.id " +
                     "WHERE p.status = 'Menunggu Koorprodi'";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                    int id = rs.getInt("id");
                    int idMahasiswa = rs.getInt("id_mahasiswa");
                    String tanggal = rs.getString("tanggal");
                    int durasi = rs.getInt("durasi_hari");
                    String status = rs.getString("status");
                    String jenis = rs.getString("jenis_izin");

                    PengajuanIzin izinObj = null;

                    if (jenis.equals("Sakit")) {
                        boolean adaSurat = rs.getInt("ada_surat_dokter") == 1;
                        izinObj = new IzinSakit(id, idMahasiswa, tanggal, durasi, status, adaSurat);
                    } else if (jenis.equals("Penting")){
                        String kategori = rs.getString("kategori");
                        izinObj = new IzinPenting(id, idMahasiswa, tanggal, durasi, status, kategori);

                    if (izinObj != null) {
                        izinObj.setNamaMahasiswa(rs.getString("nama"));
                        izinObj.setNimMahasiswa(rs.getString("nim"));

                        daftarIzin.add(izinObj);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[DB ERROR] Gagal mengambil antrean Koorprodi: " + e.getMessage());
        }
        return daftarIzin;
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