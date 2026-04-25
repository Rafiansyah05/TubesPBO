package com.telkom.alumni.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admins")
public class Admin extends User {
    
    @Column(name = "jabatan")
    private String jabatan;
    
    @OneToMany(fetch = FetchType.LAZY)
    private List<Alumni> daftarAlumni = new ArrayList<>();
    
    public Admin() {}
    
    public Admin(String idUser, String name, String email, String password, String jabatan) {
        super(idUser, name, email, password);
        this.jabatan = jabatan;
    }
    
    @Override
    public String getProfile() {
        return "=== Profile Admin ===\n" +
               "Nama    : " + getName() + "\n" +
               "Jabatan : " + jabatan;
    }
    
    public void tambahAlumni(Alumni alumni) {
        daftarAlumni.add(alumni);
        System.out.println("Alumni " + alumni.getName() + " berhasil ditambahkan.");
    }
    
    public void deleteAlumni(String idAlumni) {
        daftarAlumni.removeIf(alumni -> alumni.getIdUser().equals(idAlumni));
        System.out.println("Alumni berhasil dihapus.");
    }
    
    public void verifyAlumni(Alumni alumni) {
        System.out.println("Alumni " + alumni.getName() + " berhasil diverifikasi.");
    }
    
    public List<Alumni> cariAlumniByNama(String keyword) {
        List<Alumni> hasil = new ArrayList<>();
        for (Alumni alumni : daftarAlumni) {
            if (alumni.getName().toLowerCase().contains(keyword.toLowerCase())) {
                hasil.add(alumni);
            }
        }
        return hasil;
    }
    
    public String getJabatan() { return jabatan; }
    public void setJabatan(String jabatan) { this.jabatan = jabatan; }
    
    public List<Alumni> getDaftarAlumni() { return daftarAlumni; }
    public void setDaftarAlumni(List<Alumni> daftarAlumni) { this.daftarAlumni = daftarAlumni; }
}