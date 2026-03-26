package com.pnc.izin.entity;

public class PengajuanIzin {
    protected int id;
    protected int idMahasiswa; 
    protected String tanggal;
    protected int durasiHari;
    protected String status; 
    protected String namaMahasiswa = "";
    protected String nimMahasiswa = "";

    // Constructor
    public PengajuanIzin() {

    }
    
    public PengajuanIzin(int id, int idMahasiswa, String tanggal, int durasiHari, String status) {
        this.id = id;
        this.idMahasiswa = idMahasiswa;
        this.tanggal = tanggal;
        this.durasiHari = durasiHari;
        this.status = status;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMahasiswa() {
        return idMahasiswa;
    }

    public void setIdMahasiswa(int idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getDurasiHari() {
        return durasiHari;
    }

    public void setDurasiHari(int durasiHari) {
        this.durasiHari = durasiHari;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNamaMahasiswa(String namaMahasiswa) {
        this.namaMahasiswa = namaMahasiswa;
    }

    public void setNimMahasiswa(String nimMahasiswa) {
        this.nimMahasiswa = nimMahasiswa;
    }
    
    // --- METHOD BEHAVIOR ---
    /**
     * Method untuk menampilkan detail surat izin
     */
    public void tampilkanDetail() {
        System.out.println("===== DETAIL PENGAJUAN IZIN =====");
        System.out.println("ID          : " + id);

        if (!namaMahasiswa.isEmpty()) {
            System.out.println("Mahasiswa   : " + namaMahasiswa + " (" + nimMahasiswa + ")");
        } else {
            System.out.println("ID Mahasiswa    : " + idMahasiswa);
        }

        System.out.println("Tanggal     : " + tanggal);
        System.out.println("Durasi      : " + durasiHari + " hari");
        System.out.println("Status      : " + status);
    }
}