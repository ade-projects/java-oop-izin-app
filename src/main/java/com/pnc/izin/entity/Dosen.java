package com.pnc.izin.entity;

public class Dosen extends User {
    private String nip;
    private boolean isDosenWali;
    private boolean isKoorProdi;

    public Dosen() {
        super();
    }

    public Dosen (int id, String nama, String role, String nip, boolean isDosenWali, boolean isKoorProdi) {
        super(id, nama, role);
        this.nip = nip;
        this.isDosenWali = isDosenWali;
        this.isKoorProdi = isKoorProdi;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public boolean isDosenWali() {
        return isDosenWali;
    }

    public void setDosenWali(boolean isDosenWali) {
        this.isDosenWali = isDosenWali;
    }

    public boolean isKoorProdi() {
        return isKoorProdi;
    }

    public void setKoorProdi(boolean isKoorProdi) {
        this.isKoorProdi = isKoorProdi;
    }

    // --- METHOD BEHAVIOR ---
    public String tentukanStatusDariWali(int durasiHari) {
        if (durasiHari > 1) {
            System.out.println("[SYSTEM DOSEN] Durasi lebih dari 1 hari. Meneruskan ke Koordinator Prodi...");
            return "Menunggu Koorprodi";
        } else {
            System.out.println("[SYSTEM DOSEN] Durasi 1 hari. Persetujuan final diberikan.");
            return "Disetujui";
        }
    }

    @Override
    public void tampilkanProfil() {
        super.tampilkanProfil();
        System.out.println("NIP         : " + nip);
        String teksJabatan = "";
        if (isDosenWali && isKoorProdi) {
            teksJabatan = "Dosen Wali sekaligus Koordinator Prodi";
        } else if (isDosenWali) {
            teksJabatan = "Dosen Wali";
        } else if (isKoorProdi) {
            teksJabatan = "Koordinator Prodi";
        } else {
            teksJabatan = "Dosen Pengajar";
        }

        System.out.println("Jabatan     : " + teksJabatan);
    }
}