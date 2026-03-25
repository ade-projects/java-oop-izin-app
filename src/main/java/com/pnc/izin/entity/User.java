package com.pnc.izin.entity;

public class User {
    protected int id;
    protected String nama;
    protected String role;

    public User() {
        
    }

    public User(int id, String nama, String role) {
        this.id = id;
        this.nama = nama;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Method untuk menampilkan profil dasar
     */
    public void tampilkanProfil() {
        System.out.println("\n===== PROFIL PENGGUNA =====");
        System.out.println("ID          : " + id);
        System.out.println("Nama        : " + nama);
        System.out.println("Role        : " + role);
    }
}