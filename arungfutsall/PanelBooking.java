/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package arungfutsall;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PanelBooking extends JPanel {
    JTextField tfNama, tfNoHP, tfTanggal;
    JComboBox<String> cbLapangan, cbJamMulai, cbJamSelesai;
    JTable table;
    DefaultTableModel tableModel;
    Connection conn;

    public PanelBooking() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 45, 26)); // Futsal green

        // Koneksi database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/arung_futsal", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal koneksi ke database.");
        }

        // ================== FORM INPUT ==================
        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBackground(new Color(34, 102, 34));
        panelInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(184, 134, 11), 2, true),
                "PEMESANAN FUTSAL",
                0, 0, new Font("Segoe UI", Font.BOLD, 16),
                new Color(255, 215, 0)
            ),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Label dan Field
        tfNama = new JTextField(14);
        tfNoHP = new JTextField(14);
        tfTanggal = new JTextField("yyyy-mm-dd", 14);
        cbLapangan = new JComboBox<>(new String[]{"A", "B", "C"});
        cbJamMulai = new JComboBox<>(generateJamMulai());
        cbJamSelesai = new JComboBox<>(generateJamSelesai());

        int row = 0;
        panelInputAdd(panelInput, gbc, row++, "Nama Pelanggan:", tfNama);
        panelInputAdd(panelInput, gbc, row++, "Lapangan:", cbLapangan);
        panelInputAdd(panelInput, gbc, row++, "Tanggal:", tfTanggal);
        panelInputAdd(panelInput, gbc, row++, "Jam Mulai:", cbJamMulai);
        panelInputAdd(panelInput, gbc, row++, "Jam Selesai:", cbJamSelesai);
        panelInputAdd(panelInput, gbc, row++, "No HP:", tfNoHP);

        add(panelInput, BorderLayout.NORTH);

        // ================== TABLE ==================
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Lapangan", "Tanggal", "Mulai", "Selesai", "HP"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(22);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(41, 84, 35));
        table.getTableHeader().setForeground(Color.WHITE);
        loadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================== BUTTON PANEL ==================
        JPanel panelButton = new JPanel();
        panelButton.setBackground(new Color(18, 45, 26));
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        styleButton(btnTambah);
        styleButton(btnEdit);
        styleButton(btnHapus);
        panelButton.add(btnTambah);
        panelButton.add(btnEdit);
        panelButton.add(btnHapus);
        add(panelButton, BorderLayout.SOUTH);

        // Action listeners
        btnTambah.addActionListener(e -> addBooking());
        btnEdit.addActionListener(e -> editBooking());
        btnHapus.addActionListener(e -> deleteBooking());
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { selectBooking(); }
        });
    }

    private void panelInputAdd(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = y;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(255, 215, 0)); // Gold
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(new Color(18, 45, 26));
        button.setBorder(BorderFactory.createLineBorder(new Color(184, 134, 11), 2, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
    }

    private void addBooking() {
        String nama = tfNama.getText();
        String lapangan = cbLapangan.getSelectedItem().toString();
        String tanggal = tfTanggal.getText();
        String jamMulai = cbJamMulai.getSelectedItem().toString();
        String jamSelesai = cbJamSelesai.getSelectedItem().toString();
        String noHp = tfNoHP.getText();

        if (isWaktuTabrakan(tanggal, lapangan, jamMulai, jamSelesai, -1)) {
            JOptionPane.showMessageDialog(this, "Jadwal bentrok! Sudah ada penyewaan di waktu tersebut.");
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO penyewaan (nama_pelanggan, lapangan, tanggal, jam_mulai, jam_selesai, no_hp) VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, nama);
            ps.setString(2, lapangan);
            ps.setString(3, tanggal);
            ps.setString(4, jamMulai);
            ps.setString(5, jamSelesai);
            ps.setString(6, noHp);
            ps.executeUpdate();
            loadTable();
            clearForm();
            updatePanels();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void editBooking() {
        int row = table.getSelectedRow();
        if (row != -1) {
            try {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                if (isWaktuTabrakan(tfTanggal.getText(), cbLapangan.getSelectedItem().toString(),
                        cbJamMulai.getSelectedItem().toString(), cbJamSelesai.getSelectedItem().toString(), id)) {
                    JOptionPane.showMessageDialog(this, "Jadwal bentrok! Sudah ada penyewaan lain di waktu tersebut.");
                    return;
                }
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE penyewaan SET nama_pelanggan=?, lapangan=?, tanggal=?, jam_mulai=?, jam_selesai=?, no_hp=? WHERE id=?"
                );
                ps.setString(1, tfNama.getText());
                ps.setString(2, cbLapangan.getSelectedItem().toString());
                ps.setString(3, tfTanggal.getText());
                ps.setString(4, cbJamMulai.getSelectedItem().toString());
                ps.setString(5, cbJamSelesai.getSelectedItem().toString());
                ps.setString(6, tfNoHP.getText());
                ps.setInt(7, id);
                ps.executeUpdate();
                loadTable();
                clearForm();
                updatePanels();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void deleteBooking() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            try {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM penyewaan WHERE id = ?");
                ps.setInt(1, id);
                ps.executeUpdate();
                loadTable();
                clearForm();
                updatePanels();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void selectBooking() {
        int row = table.getSelectedRow();
        tfNama.setText(tableModel.getValueAt(row, 1).toString());
        cbLapangan.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        tfTanggal.setText(tableModel.getValueAt(row, 3).toString());
        cbJamMulai.setSelectedItem(tableModel.getValueAt(row, 4).toString());
        cbJamSelesai.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        tfNoHP.setText(tableModel.getValueAt(row, 6).toString());
    }

    private boolean isWaktuTabrakan(String tanggal, String lapangan, String jamMulai, String jamSelesai, int idPengecualian) {
        try {
            String sql = """
                    SELECT COUNT(*) FROM penyewaan
                    WHERE tanggal = ? AND lapangan = ?
                    AND NOT (jam_selesai <= ? OR jam_mulai >= ?)
                    AND id != ?
                """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tanggal);
            ps.setString(2, lapangan);
            ps.setString(3, jamMulai);
            ps.setString(4, jamSelesai);
            ps.setInt(5, idPengecualian);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void clearForm() {
        tfNama.setText("");
        tfTanggal.setText("yyyy-mm-dd");
        tfNoHP.setText("");
        cbLapangan.setSelectedIndex(0);
        cbJamMulai.setSelectedIndex(0);
        cbJamSelesai.setSelectedIndex(0);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM penyewaan");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nama_pelanggan"),
                        rs.getString("lapangan"),
                        rs.getDate("tanggal"),
                        rs.getTime("jam_mulai"),
                        rs.getTime("jam_selesai"),
                        rs.getString("no_hp")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String[] generateJamMulai() {
        String[] jam = new String[15]; // 08:00 - 22:00
        for (int i = 0; i < 15; i++) {
            jam[i] = String.format("%02d:00:00", i + 8);
        }
        return jam;
    }

    private String[] generateJamSelesai() {
        String[] jam = new String[16]; // 08:00 - 23:00
        for (int i = 0; i < 16; i++) {
            jam[i] = String.format("%02d:00:00", i + 8);
        }
        return jam;
    }

    private void updatePanels() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof DashboardAdmin parent) {
            if (parent.panelLaporan != null) parent.panelLaporan.loadLaporan();
            if (parent.panelManajemenLapangan != null) {
                parent.panelManajemenLapangan.refreshTanggal();
                if (parent.panelManajemenLapangan.cbTanggal.getItemCount() > 0) {
                    String tanggal = (String) parent.panelManajemenLapangan.cbTanggal.getSelectedItem();
                    if (tanggal != null) parent.panelManajemenLapangan.tampilkanJadwal(tanggal);
                }
            }
        }
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
