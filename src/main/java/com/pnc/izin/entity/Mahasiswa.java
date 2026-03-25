package com.pnc.izin.entity;

public class Mahasiswa extends User {
    private String nim;
    private int idDosenWali;
    private int totalJamAlpa;

    public Mahasiswa() {
        super();
    }

    public Mahasiswa(int id, String nama, String role, String nim, int idDosenWali, int totalJamAlpa) {
        super(id, nama, role);
        this.nim = nim;
        this.idDosenWali = idDosenWali;
        this.totalJamAlpa = totalJamAlpa;
    }
    
    public String getNim() {
        return nim;
    }

    public int getIdDosenWali() {
        return idDosenWali;
    }

    public int getTotalJamAlpa() {
        return totalJamAlpa;
    }
    
    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setIdDosenWali(int idDosenWali) {
        this.idDosenWali = idDosenWali;
    }

    public void setTotalJamAlpa(int totalJamAlpa) {
        this.totalJamAlpa = totalJamAlpa;
    }

    /**
     * Method khusus menambah jam alpa
     */
    public void tambahJamAlpa(int jam) {
        this.totalJamAlpa += jam;
        System.out.println("[SUCCESS] " + jam + " Jam alpa berhasil ditambahkan.");
    }

    /**
     * Method menghitung total kompensasi sesuai aturan kampus
     */
    public int hitungKompensasi() {
        return this.totalJamAlpa * 3;
    }

    private String namaDosenWali = "Belum diatur";

    public void setNamaDosenWali(String namaDosenWali) {
        this.namaDosenWali = namaDosenWali;
    }

    /**
     * Override method dari class induk untuk menampilkan data yang lebih lengkap
     */
    @Override
    public void tampilkanProfil() {
        super.tampilkanProfil();
        System.out.println("NIM         : " + nim);
        System.out.println("Dosen Wali  : " + namaDosenWali);
        System.out.println("Alpa        : " + totalJamAlpa + " jam");
        System.out.println("Kompensasi  : " + hitungKompensasi() + " jam");
    }
}