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
    
    @Override
    public void tampilkanProfil() {
        super.tampilkanProfil();
        System.out.println("NIP         : " + nip);
    }
}