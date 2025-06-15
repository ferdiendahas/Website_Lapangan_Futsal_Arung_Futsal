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

public class PanelInventaris extends JPanel {
    private JTextField tfName, tfType, tfPrice, tfStock;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection conn;

    public PanelInventaris() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 128, 0)); // Light background

        // HEADER
        JLabel lblTitle = new JLabel("Inventaris Peralatan", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 20));
        lblTitle.setForeground(new Color(255, 215, 0));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(14, 0, 8, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Koneksi database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/arung_futsal", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal koneksi ke database: " + e.getMessage());
        }

        // ============== PANEL INPUT CARD ==============
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(34, 102, 34));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(184, 134, 11), 2, true),
                "INPUT DATA INVENTARIS",
                0, 0, new Font("Segoe UI", Font.BOLD, 16),
                new Color(255, 215, 0)
            ),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;

        tfName  = new JTextField(15);
        tfType  = new JTextField(15);
        tfPrice = new JTextField(15);
        tfStock = new JTextField(15);

        int row = 0;
        panelInputAdd(inputPanel, gbc, row++, "Nama Barang:", tfName);
        panelInputAdd(inputPanel, gbc, row++, "Jenis:", tfType);
        panelInputAdd(inputPanel, gbc, row++, "Harga:", tfPrice);
        panelInputAdd(inputPanel, gbc, row++, "Stok:", tfStock);

        // BUTTON CRUD
        JPanel panelBtn = new JPanel();
        panelBtn.setOpaque(false);
        JButton btnAdd = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");
        styleButton(btnAdd); styleButton(btnUpdate); styleButton(btnDelete);
        panelBtn.add(btnAdd); panelBtn.add(btnUpdate); panelBtn.add(btnDelete);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(panelBtn, gbc);

        add(inputPanel, BorderLayout.WEST);

        // =============== TABLE ================
        String[] columnNames = {"ID", "Nama Barang", "Jenis", "Harga", "Stok"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);
        // Header style: hijau tua, font kuning
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(29, 61, 15));
        table.getTableHeader().setForeground(Color.black);
        table.setBackground(new Color(230, 255, 250));

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // Load awal
        loadTable();

        // =============== ACTIONS ===============
        btnAdd.addActionListener(e -> addItem());
        btnUpdate.addActionListener(e -> updateItem());
        btnDelete.addActionListener(e -> deleteItem());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    tfName.setText(tableModel.getValueAt(row, 1).toString());
                    tfType.setText(tableModel.getValueAt(row, 2).toString());
                    tfPrice.setText(tableModel.getValueAt(row, 3).toString());
                    tfStock.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });
    }

    private void panelInputAdd(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(255, 215, 0));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(new Color(18, 45, 26));
        button.setBorder(BorderFactory.createLineBorder(new Color(184, 134, 11), 2, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM inventaris_peralatan ORDER BY id_peralatan DESC");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_peralatan"),
                    rs.getString("nama_peralatan"),
                    rs.getString("jenis_peralatan"),
                    rs.getDouble("harga"),
                    rs.getInt("jumlah_stok")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addItem() {
        String name = tfName.getText();
        String type = tfType.getText();
        double price;
        int stock;
        try {
            price = Double.parseDouble(tfPrice.getText());
            stock = Integer.parseInt(tfStock.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus angka!");
            return;
        }
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO inventaris_peralatan (nama_peralatan, jenis_peralatan, harga, jumlah_stok) VALUES (?, ?, ?, ?)"
            );
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.executeUpdate();
            loadTable();
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal tambah data: " + ex.getMessage());
        }
    }

    private void updateItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih barang untuk diupdate.");
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        String name = tfName.getText();
        String type = tfType.getText();
        double price;
        int stock;
        try {
            price = Double.parseDouble(tfPrice.getText());
            stock = Integer.parseInt(tfStock.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus angka!");
            return;
        }
        try {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE inventaris_peralatan SET nama_peralatan=?, jenis_peralatan=?, harga=?, jumlah_stok=? WHERE id_peralatan=?"
            );
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setInt(5, id);
            ps.executeUpdate();
            loadTable();
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal update data: " + ex.getMessage());
        }
    }

    private void deleteItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih barang untuk dihapus.");
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        try {
            PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM inventaris_peralatan WHERE id_peralatan=?"
            );
            ps.setInt(1, id);
            ps.executeUpdate();
            loadTable();
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal hapus data: " + ex.getMessage());
        }
    }

    private void clearFields() {
        tfName.setText("");
        tfType.setText("");
        tfPrice.setText("");
        tfStock.setText("");
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
