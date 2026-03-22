package com.pnc.izin.entity;

public class IzinSakit extends PengajuanIzin {
    private boolean adaSuratDokter;

    // Constructor
    public IzinSakit() {
        super();
    }    

    public IzinSakit(int id, int idMahasiswa, String tanggal, int durasiHari, String status, boolean adaSuratDokter) {
        super(id, idMahasiswa, tanggal, durasiHari, status);
        this.adaSuratDokter = adaSuratDokter;
    }

    // Getter & Setter
    public boolean isAdaSuratDokter() {
        return adaSuratDokter;
    }

    public void setAdaSuratDokter(boolean adaSuratDokter) {
        this.adaSuratDokter = adaSuratDokter;
    }

    // --- METHOD BEHAVIOR ---

    /**
     * Method untuk memvalidasi apakah dokumen pendukung (surat dokter) sudah lengkap.
     */
    public boolean validasiDokumen() {
        if (this.durasiHari >= 1 && !this.adaSuratDokter) {
            System.out.println("[REJECT] Izin sakit 1 hari atau lebih WAJIB melampirkan surat dokter!");
            return false;
        }
        System.out.println("[OK] Dokumen izin sakit valid.");
        return true;
    }

    @Override
    public void tampilkanDetail() {
        super.tampilkanDetail();
        if (adaSuratDokter) {
            System.out.println("Surat Dokter: Ada" );
        } else {
            System.out.println("Surat Dokter: Tidak Ada" );
        }
    }
}