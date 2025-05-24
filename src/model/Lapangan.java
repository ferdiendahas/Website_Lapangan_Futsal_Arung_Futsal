/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Lapangan {
    private String nama;
    private double harga;
    private boolean status;

    public Lapangan(String nama, double harga, boolean status) {
        this.nama = nama;
        this.harga = harga;
        this.status = status;
    }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
