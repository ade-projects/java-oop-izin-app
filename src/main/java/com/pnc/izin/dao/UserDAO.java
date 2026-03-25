package com.pnc.izin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.pnc.izin.config.DatabaseHelper;
import com.pnc.izin.entity.Admin;
import com.pnc.izin.entity.Dosen;
import com.pnc.izin.entity.Mahasiswa;

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

                    Dosen dosen = getDosenById(idDosenWali);
                    if (dosen != null) {
                        mhs.setNamaDosenWali(dosen.getNama());
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengambil data mahasiswa: " + e.getMessage());
        }

        return mhs;
    }

    /**
     * Method khusus untuk menyuntikkan data Dosen ke database.
     */
    public void tambahDosen(Dosen dosen) {
        String sql = "INSERT INTO user (nama, role, nip, is_dosen_wali, is_koor_prodi) VALUES (?, ?, ?, ?, ?)";

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

    /**
     * Menyimpan data Admin baru ke database
     */
    public void tambahAdmin(Admin admin) {
        String sql = "INSERT INTO user (nama, role, nip) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, admin.getNama());
            pstmt.setString(2, admin.getRole());
            pstmt.setString(3, admin.getNip());
            
            pstmt.executeUpdate();
            System.out.println("[DB SUCCESS] Data Admin (" + admin.getNama() + ") berhasil disimpan!");
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal menyimpan data admin: " + e.getMessage());
        }
    }

    /**
     * Mencari Admin berdasarkan NIP untuk fitur Login
     */
    public Admin getAdminByNip(String nipInput) {
        String sql = "SELECT * FROM user WHERE role = 'Admin' AND nip = ?";
        Admin admin = null;
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nipInput);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    admin = new Admin(
                        rs.getInt("id"), 
                        rs.getString("nama"), 
                        rs.getString("role"), 
                        rs.getString("nip")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengambil data admin: " + e.getMessage());
        }
        return admin;
    }

    /**
     * Menampilkan seluruh pengguna yang ada di sistem (Read All)
     */
    public ArrayList<String> getSemuaUserList() {
        ArrayList<String> listUser = new ArrayList<>();

        String sql = "SELECT id, nama, role, nim, nip FROM user";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String identitas = rs.getString("role").equals("Mahasiswa") ? rs.getString("nim") : rs.getString("nip");
                String dataBaris = String.format("%-5d | %-30s | %-15s | %-15s", 
                    rs.getInt("id"), rs.getString("nama"), rs.getString("role"), identitas);
                listUser.add(dataBaris);
            }
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal menampilkan data pengguna: " + e.getMessage());
        }
        return listUser;
    }

    /**
     * Menghapus pengguna dari database berdasarkan ID (Delete)
     */
    public void hapusUser(int idUser) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUser);
            int barisTerhapus = pstmt.executeUpdate();
            
            if (barisTerhapus > 0) {
                System.out.println("[DB SUCCESS] Pengguna dengan ID " + idUser + " berhasil dihapus dari sistem!");
            } else {
                System.out.println("[DB WARNING] Pengguna dengan ID " + idUser + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal menghapus pengguna: " + e.getMessage());
        }
    }

    /**
     * Mengecek role dari sebuah ID untuk menentukan menu update yang tepat
     */
    public String getRoleById(int id) {
        String sql = "SELECT role FROM user WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getString("role");
            }
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengecek role: " + e.getMessage());
        }
        return null;
    }

    /**
     * Method get by ID
     */
    public Mahasiswa getMahasiswaById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    return new Mahasiswa(
                        rs.getInt("id"), rs.getString("nama"), rs.getString("role"), rs.getString("nim"), rs.getInt("id_dosen_wali"), rs.getInt("total_jam_alpa"));
                }
            } 
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengambil data Mahasiswa: " + e.getMessage());
        }
        return null;
    }

    public Dosen getDosenById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    return new Dosen(
                        rs.getInt("id"), rs.getString("nama"), rs.getString("role"), rs.getString("nip"), rs.getInt("is_dosen_wali") == 1, rs.getInt("is_koor_prodi") == 1);
                }
            } 
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengambil data Dosen: " + e.getMessage());
        }
        return null;
    }

    public Admin getAdminById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    return new Admin(
                        rs.getInt("id"), rs.getString("nama"), rs.getString("role"), rs.getString("nip"));
                }
            } 
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal mengambil data Admin: " + e.getMessage());
        }
        return null;
    }

    /**
     * Method update user
     */
    public void updateMahasiswa(Mahasiswa mhs) {
        String sql = "UPDATE user SET nama = ?, nim = ?, id_dosen_wali = ?,total_jam_alpa = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql) ) {
            pstmt.setString(1, mhs.getNama());
            pstmt.setString(2, mhs.getNim());
            pstmt.setInt(3, mhs.getIdDosenWali());
            pstmt.setInt(4, mhs.getTotalJamAlpa());
            pstmt.setInt(5, mhs.getId());
            pstmt.executeUpdate();
            System.out.println("[DB SUCCESS] Data Mahasiswa berhasil diupdate!");
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal update data Mahasiswa: " + e.getMessage());
        }
    }

    public void updateDosen(Dosen dosen) {
        String sql = "UPDATE user SET nama = ?, nip = ?, is_dosen_wali = ?,is_koor_prodi = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql) ) {
            pstmt.setString(1, dosen.getNama());
            pstmt.setString(2, dosen.getNip());
            pstmt.setInt(3, dosen.isDosenWali() ? 1 : 0);
            pstmt.setInt(4, dosen.isKoorProdi() ? 1 : 0) ;
            pstmt.setInt(5, dosen.getId());
            pstmt.executeUpdate();
            System.out.println("[DB SUCCESS] Data Dosen berhasil diupdate!");
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal update data Dosen: " + e.getMessage());
        }
    }

    public void updateAdmin(Admin admin) {
        String sql = "UPDATE user SET nama = ?, nip = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql) ) {
            pstmt.setString(1, admin.getNama());
            pstmt.setString(2, admin.getNip());
            pstmt.setInt(5, admin.getId());
            pstmt.executeUpdate();
            System.out.println("[DB SUCCESS] Data Admin berhasil diupdate!");
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal update data Admin: " + e.getMessage());
        }
    }

    /**
     * Method sementara untuk keperluan presentasi (menampilkan akun demo)
     */
    public void tampilkanUserDemo(String roleFilter) {
        String sql = "SELECT * FROM user WHERE role = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roleFilter);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n--- [AKUN DEMO] DAFTAR " + roleFilter.toUpperCase() + " ---");
                boolean adaData = false;
                
                while (rs.next()) {
                    adaData = true;
                    String nama = rs.getString("nama");
                    String identitas = roleFilter.equals("Mahasiswa") ? rs.getString("nim") : rs.getString("nip");
                    
                    // Cek jabatan khusus untuk Dosen
                    String infoJabatan = "";
                    if (roleFilter.equals("Dosen")) {
                        boolean isWali = rs.getInt("is_dosen_wali") == 1;
                        boolean isKoor = rs.getInt("is_koor_prodi") == 1;
                        if (isWali && isKoor) infoJabatan = " (Dosen Wali & Koorprodi)";
                        else if (isWali) infoJabatan = " (Dosen Wali)";
                        else if (isKoor) infoJabatan = " (Koorprodi)";
                        else infoJabatan = " (Dosen Pengajar)";
                    }
                    
                    System.out.println("- " + nama + " | " + identitas + infoJabatan);
                }
                
                if (!adaData) {
                    System.out.println("(Belum ada data " + roleFilter + " di database)");
                }
                System.out.println("----------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("[DB ERROR] Gagal memuat user demo: " + e.getMessage());
        }
    }
}