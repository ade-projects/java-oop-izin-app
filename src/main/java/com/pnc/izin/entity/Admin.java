package com.pnc.izin.entity;

public class Admin extends User {
    private String nip;

    public Admin() {
        super();
    }

    public Admin(int id, String nama, String role, String nip) {
        super(id, nama, role);
        this.nip = nip;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }
    
    // --- METHOD BEHAVIOR (STUB) ---
    // public void tambahPengguna() {
    //     System.out.println("[ADMIN] Membuka menu tambah pengguna...");
    // }

    // public void tampilkanPengguna() {
    //     System.out.println("[ADMIN] Menampilkan seluruh daftar pengguna sistem...");
    // }

    // public void updatePengguna() {
    //     System.out.println("[ADMIN] Membuka menu update data pengguna...");
    // }

    // public void hapusPengguna() {
    //     System.out.println("[ADMIN] Membuka menu hapus pengguna...");
    // }

    @Override
    public void tampilkanProfil() {
        super.tampilkanProfil();
        System.out.println("NIP         : " + nip);
    }
}