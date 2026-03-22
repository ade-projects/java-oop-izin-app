package com.pnc.izin.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    
    // URL DB
    private static final String DB_URL = "jdbc:sqlite:data_izin.db";

    /**
     * Method untuk membuka koneksi ke database.
     */
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            conn.createStatement().execute("PRAGMA foreign_keys = ON;");
            return conn;
        } catch (SQLException e) {
            System.out.println("[Error] " + e.getMessage());
            return null;
        }
    }

    /**
     * Method untuk menyiapkan database.
     */
    public static void setupTabel() {
        String sqlUser = "CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY AUTOINCREMENT, nama TEXT NOT NULL, role TEXT NOT NULL, nip TEXT, nim TEXT, id_dosen_wali INTEGER, total_jam_alpa INTEGER DEFAULT 0, is_dosen_wali INTEGER DEFAULT 0, is_koor_prodi INTEGER DEFAULT 0);";

        String sqlIzin = "CREATE TABLE IF NOT EXISTS pengajuan_izin (id INTEGER PRIMARY KEY AUTOINCREMENT, id_mahasiswa INTEGER NOT NULL, tanggal TEXT NOT NULL, durasi_hari INTEGER NOT NULL, status TEXT NOT NULL, jenis_izin TEXT NOT NULL, ada_surat_dokter INTEGER, kategori TEXT, FOREIGN KEY (id_mahasiswa) REFERENCES user(id) ON DELETE CASCADE);";

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUser);
            stmt.execute(sqlIzin);

            System.out.println("Database berhasil disiapkan.");
        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}