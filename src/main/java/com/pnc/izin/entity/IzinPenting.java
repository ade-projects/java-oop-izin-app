package com.pnc.izin.entity;

public class IzinPenting extends PengajuanIzin {
    private String kategori;

    // Constructor
    public IzinPenting() {
        super();
    }
    
    public IzinPenting(int id, int idMahasiswa, String tanggal, int durasiHari, String status, String kategori) {
        super(id, idMahasiswa, tanggal, durasiHari, status);
        this.kategori = kategori;
    }

    // Buat Getter & Setter
    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    // --- METHOD BEHAVIOR ---

    /**
     * Method untuk memvalidasi batas maksimal hari berdasarkan kategori.
     */
    public boolean validasiBatasHari() {
        String kat = this.kategori.toLowerCase();

        if (kat.equals("keluarga")) {
            if (this.durasiHari > 3) {
                System.out.println("[REJECT] Izin urusan keluarga maksimal hanya 3 hari!");
                return false;
            }
        } else if (kat.equals("pribadi")) {
            if (this.durasiHari > 1) {
                System.out.println("[REJECT] Izin urusan pribadi maksimal hanya 1 hari!");
                return false;
            }
        } else if (kat.equals("tugas") || kat.equals("dispensasi")) {
            System.out.println("[INFO] Kategori Tugas/Dispensasi terdeteksi. Durasi " + this.durasiHari + " hari diizinkan.");
            System.out.println("[NOTE] WAJIB menyerahkan Surat Tugas resmi ke Dosen Wali!");
            return true;
        } else {
            System.out.println("[REJECT] Kategori izin '" + this.kategori + "' tidak valid di sistem kami.");
            return false;
        }

        System.out.println("[OK] Durasi izin penting masih dalam batas wajar.");
        return true;
    }
    
    @Override
    public void  tampilkanDetail() {
        super.tampilkanDetail();
        System.out.println("Kategori    : " + kategori);
    }
}