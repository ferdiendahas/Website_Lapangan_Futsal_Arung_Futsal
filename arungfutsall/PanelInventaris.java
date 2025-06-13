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

        // Koneksi ke database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/arung_futsal", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal koneksi ke database: " + e.getMessage());
        }

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Nama Barang
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Nama Barang:"), gbc);
        tfName = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfName, gbc);
        
        // Jenis
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Jenis:"), gbc);
        tfType = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfType, gbc);

        // Harga
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Harga:"), gbc);
        tfPrice = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfPrice, gbc);

        // Stok
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Stok:"), gbc);
        tfStock = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfStock, gbc);

        // Tombol
        JButton btnAdd = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(btnAdd, gbc);
        gbc.gridx = 1;
        inputPanel.add(btnUpdate, gbc);
        gbc.gridx = 2;
        inputPanel.add(btnDelete, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"ID", "Nama Barang", "Jenis", "Harga", "Stok"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadTable();
        
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
