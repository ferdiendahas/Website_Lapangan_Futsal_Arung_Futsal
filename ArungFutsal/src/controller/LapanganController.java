/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Lapangan;
import java.util.*;
import java.util.stream.Collectors;

public class LapanganController {
    private List<Lapangan> lapanganList = new ArrayList<>();

    public void tambahLapangan(Lapangan l) { lapanganList.add(l); }

    public void editLapangan(int index, Lapangan l) {
        if (index >= 0 && index < lapanganList.size()) {
            lapanganList.set(index, l);
        }
    }

    public void hapusLapangan(int index) {
        if (index >= 0 && index < lapanganList.size()) {
            lapanganList.remove(index);
        }
    }

    public List<Lapangan> cariLapangan(String keyword) {
        return lapanganList.stream()
                .filter(l -> l.getNama().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Lapangan> getAllLapangan() {
        return lapanganList;
    }
}
