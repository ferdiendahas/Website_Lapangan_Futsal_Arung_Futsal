package PenjadwalanAndBooking;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FormBooking extends JFrame {
    private JTextField tfNama, tfTanggal, tfJam, tfDurasi;
    private JComboBox<String> cbLapangan;
    private static ArrayList<DataBooking> bookings = new ArrayList<>();

    public FormBooking() {
        setTitle("Form Booking Lapangan");
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lbl1 = new JLabel("Nama Pelanggan:");
        JLabel lbl2 = new JLabel("Tanggal (yyyy-mm-dd):");
        JLabel lbl3 = new JLabel("Jam (HH:MM):");
        JLabel lbl4 = new JLabel("Durasi (jam):");
        JLabel lbl5 = new JLabel("Lapangan:");

        tfNama = new JTextField();
        tfTanggal = new JTextField();
        tfJam = new JTextField();
        tfDurasi = new JTextField();
        cbLapangan = new JComboBox<>(new String[]{"Lapangan A", "Lapangan B", "Lapangan C"});

        JButton btnBooking = new JButton("Booking Sekarang");

        lbl1.setBounds(30, 20, 150, 25); tfNama.setBounds(180, 20, 150, 25);
        lbl2.setBounds(30, 50, 150, 25); tfTanggal.setBounds(180, 50, 150, 25);
        lbl3.setBounds(30, 80, 150, 25); tfJam.setBounds(180, 80, 150, 25);
        lbl4.setBounds(30, 110, 150, 25); tfDurasi.setBounds(180, 110, 150, 25);
        lbl5.setBounds(30, 140, 150, 25); cbLapangan.setBounds(180, 140, 150, 25);
        btnBooking.setBounds(120, 190, 150, 30);

        add(lbl1); add(tfNama);
        add(lbl2); add(tfTanggal);
        add(lbl3); add(tfJam);
        add(lbl4); add(tfDurasi);
        add(lbl5); add(cbLapangan);
        add(btnBooking);

        btnBooking.addActionListener(e -> prosesBooking());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prosesBooking() {
        String nama = tfNama.getText();
        String tanggal = tfTanggal.getText();
        String jam = tfJam.getText();
        String durasi = tfDurasi.getText();
        String lapangan = cbLapangan.getSelectedItem().toString();

        if (nama.isEmpty() || tanggal.isEmpty() || jam.isEmpty() || durasi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        // Simpan data
        DataBooking db = new DataBooking(nama, tanggal, jam, durasi, lapangan);
        bookings.add(db);

        // Tampilkan invoice
        new InvoiceFrame(db);
    }
}


