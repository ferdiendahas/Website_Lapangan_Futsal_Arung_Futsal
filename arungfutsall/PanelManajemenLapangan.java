/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package arungfutsall;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
/**
 *
 * @author Lenovo
 */
public class PanelManajemenLapangan extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection conn;
    public JComboBox<String> cbTanggal;

    public PanelManajemenLapangan() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 128, 0)); // Futsal green background
        
        JLabel title = new JLabel("Jadwal Booking Lapangan", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setForeground(Color.WHITE); // White font color for contrast
        add(title, BorderLayout.NORTH);

        // Top panel for date selection
        JPanel panelAtas = new JPanel(new FlowLayout());
        panelAtas.setBackground(new Color(0, 150, 0)); // Lighter green
        panelAtas.add(new JLabel("Tanggal:"));
        
        cbTanggal = new JComboBox<>();
        loadTanggalBooking();
        cbTanggal.setBackground(Color.WHITE);
        cbTanggal.setFont(new Font("Arial", Font.PLAIN, 14));
        panelAtas.add(cbTanggal);
        
        JButton btnRefresh = new JButton("Tampilkan Jadwal");
        btnRefresh.setBackground(new Color(255, 215, 0)); // Gold color for the button
        btnRefresh.setForeground(Color.BLACK);
        panelAtas.add(btnRefresh);
        add(panelAtas, BorderLayout.SOUTH);

        // Table Model
        String[] columnNames = {"Jam", "Lapangan A", "Lapangan B", "Lapangan C"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setBackground(new Color(224, 255, 255)); // Light color for table background
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/arung_futsal", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal koneksi ke database.");
        }

        // Display the schedule initially if dates are available
        if (cbTanggal.getItemCount() > 0)
            tampilkanJadwal(cbTanggal.getSelectedItem().toString());
        // Button action to refresh table view
        btnRefresh.addActionListener(e -> {
            if (cbTanggal.getSelectedItem() != null)
                tampilkanJadwal(cbTanggal.getSelectedItem().toString());
        });
        // Action listener for date combobox
        cbTanggal.addActionListener(e -> {
            if (cbTanggal.getSelectedItem() != null)
                tampilkanJadwal(cbTanggal.getSelectedItem().toString());
        });
    }

    private void loadTanggalBooking() {
        cbTanggal.removeAllItems();
        try {
            Connection localConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/arung_futsal", "root", "");
            String sql = "SELECT DISTINCT tanggal FROM penyewaan ORDER BY tanggal ASC";
            PreparedStatement ps = localConn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cbTanggal.addItem(rs.getString("tanggal"));
            }
            rs.close();
            ps.close();
            localConn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error ambil tanggal booking: " + e.getMessage());
        }
    }
    
    public void tampilkanJadwal(String tanggal) {
        tableModel.setRowCount(0);
        for (int jam = 8; jam <= 22; jam++) {
            String jamMulai = String.format("%02d:00:00", jam);
            String jamSelesai = String.format("%02d:00:00", jam + 1);

            String[] statusLap = new String[3];
            for (int i = 0; i < 3; i++) {
                String lap = switch (i) { case 0 -> "A"; case 1 -> "B"; default -> "C"; };
                statusLap[i] = cekBooking(lap, tanggal, jamMulai, jamSelesai);
            }
            tableModel.addRow(new Object[]{
                jamMulai.substring(0, 5) + " - " + jamSelesai.substring(0, 5),
                statusLap[0], statusLap[1], statusLap[2]
            });
        }
    }
    
     private String cekBooking(String lapangan, String tanggal, String jamMulai, String jamSelesai) {
        try {
            String sql = """
                SELECT nama_pelanggan FROM penyewaan
                WHERE lapangan = ? AND tanggal = ?
                AND NOT (jam_selesai <= ? OR jam_mulai >= ?)
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, lapangan);
            ps.setString(2, tanggal);
            ps.setString(3, jamMulai);
            ps.setString(4, jamSelesai);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return "Booked: " + rs.getString("nama_pelanggan");
            } else {
                return "Tersedia";
            }
        } catch (SQLException e) {
            return "Error DB";
        }
    }
     
     public void refreshTanggal() {
    loadTanggalBooking();
    if (cbTanggal.getItemCount() > 0)
        tampilkanJadwal(cbTanggal.getSelectedItem().toString());
    else
        tableModel.setRowCount(0);
}





    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
