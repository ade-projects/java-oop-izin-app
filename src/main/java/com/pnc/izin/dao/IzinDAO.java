package com.pnc.izin.dao;

import com.pnc.izin.config.DatabaseHelper;
import com.pnc.izin.entity.PengajuanIzin;
import com.pnc.izin.entity.IzinSakit;
import com.pnc.izin.entity.IzinPenting;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}