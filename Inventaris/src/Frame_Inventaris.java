
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import DataBase.DbConn;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ASUS
 */
public class Frame_Inventaris extends javax.swing.JFrame {
    private JTextField txtId;

    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Frame_Inventaris.class.getName());

    /**
     * Creates new form Frame_Inventaris
     */
    public Frame_Inventaris() {
        initComponents();

    // ID untuk update
    txtId = new JTextField(5);
    txtId.setVisible(false); // disembunyikan, hanya untuk update

    loadTableInventaris(); // Panggil fungsi untuk load data ke tabel
    Tambah.addActionListener(e -> tambahData());
    Update.addActionListener(e -> updateData());
    Hapus.addActionListener(e -> hapusData());
    }
    
    private void tambahData() {
    String nama = txtNama.getText();
    String jenis = txtJenis.getText();
    String hargaStr = txtHarga.getText();
    String stokStr = txtStok.getText();

    if (nama.isEmpty() || jenis.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua field harus diisi", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        double harga = Double.parseDouble(hargaStr);
        int stok = Integer.parseInt(stokStr);

        String sql = "INSERT INTO inventaris_peralatan (nama_peralatan, jenis_peralatan, harga, jumlah_stok) " +
                     "VALUES ('" + nama + "', '" + jenis + "', " + harga + ", " + stok + ")";

        try (Connection conn = DbConn.getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan");
            loadTableInventaris();
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka", "Format Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gagal menambah data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void hapusData() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

    try (Connection conn = DbConn.getConnection(); Statement st = conn.createStatement()) {
        boolean adaYangDipilih = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean selected = (Boolean) model.getValueAt(i, 0); // Checkbox
            if (selected != null && selected) {
                adaYangDipilih = true;
                int id = Integer.parseInt(model.getValueAt(i, 5).toString());
                st.executeUpdate("DELETE FROM inventaris_peralatan WHERE id_peralatan = " + id);
            }
        }
        if (adaYangDipilih) {
            JOptionPane.showMessageDialog(this, "Data yang dipilih berhasil dihapus.");
            loadTableInventaris();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void updateData() {
    String idStr = txtId.getText();
    String nama = txtNama.getText();
    String jenis = txtJenis.getText();
    String hargaStr = txtHarga.getText();
    String stokStr = txtStok.getText();

    if (idStr.isEmpty() || nama.isEmpty() || jenis.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua field harus diisi termasuk ID (otomatis saat pilih baris).", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        int id = Integer.parseInt(idStr);
        double harga = Double.parseDouble(hargaStr);
        int stok = Integer.parseInt(stokStr);

        String sql = "UPDATE inventaris_peralatan SET " +
                     "nama_peralatan = '" + nama + "', " +
                     "jenis_peralatan = '" + jenis + "', " +
                     "harga = " + harga + ", " +
                     "jumlah_stok = " + stok +
                     " WHERE id_peralatan = " + id;

        try (Connection conn = DbConn.getConnection(); Statement st = conn.createStatement()) {
            int affected = st.executeUpdate(sql);
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diperbarui.");
                loadTableInventaris();
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan.");
            }
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka", "Format Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memperbarui data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void loadTableInventaris() {
    String[] kolom = {"Select", "Nama Peralatan", "Jenis Peralatan", "Harga", "Jumlah Stok", "ID Peralatan"};
    DefaultTableModel model = new DefaultTableModel(null, kolom) {
        @Override
        public Class<?> getColumnClass(int column) {
            return column == 0 ? Boolean.class : super.getColumnClass(column);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0; // hanya kolom checkbox yang editable
        }
    };
    
    jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        int row = jTable1.getSelectedRow();
        if (row != -1) {
            // Asumsi kolom: 0 = checkbox, 1 = nama, 2 = jenis, 3 = harga, 4 = stok
            txtNama.setText(jTable1.getValueAt(row, 1).toString());
            txtJenis.setText(jTable1.getValueAt(row, 2).toString());
            txtHarga.setText(jTable1.getValueAt(row, 3).toString());
            txtStok.setText(jTable1.getValueAt(row, 4).toString());
            txtId.setText(jTable1.getValueAt(row, 5).toString());

            // Ambil ID dari database berdasarkan nama+jenis+harga+stok (karena ID tidak ditampilkan)
            try (Connection conn = DbConn.getConnection();
                 Statement st = conn.createStatement()) {

                String query = "SELECT id_peralatan FROM inventaris_peralatan WHERE " +
                               "nama_peralatan = '" + txtNama.getText() + "' AND " +
                               "jenis_peralatan = '" + txtJenis.getText() + "' AND " +
                               "harga = " + txtHarga.getText() + " AND " +
                               "jumlah_stok = " + txtStok.getText() + " LIMIT 1";
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    txtId.setText(String.valueOf(rs.getInt("id_peralatan")));
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal mengambil ID: " + e.getMessage());
            }
        }
    }
});

    jTable1.setModel(model);
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);

    // Kolom yang rata tengah (kecuali 0 = checkbox, 7 & 8 = created/updated)
    for (int i = 1; i < jTable1.getColumnCount(); i++) {
        if (i != 7 && i != 8) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
        // Set warna latar belakang tabel jadi putih
    jTable1.setBackground(Color.WHITE);

    // Set tampilan header kolom
    JTableHeader header = jTable1.getTableHeader();
    header.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 12));
    ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);


    TableColumnModel columnModel = jTable1.getColumnModel();
    TableColumn selectColumn = columnModel.getColumn(0);
    selectColumn.setPreferredWidth(30);
    selectColumn.setMaxWidth(50);
    selectColumn.setMinWidth(50);
    selectColumn.setResizable(false);    // Checkbox
    columnModel.getColumn(0).setPreferredWidth(150);  // Nama Peralatan
    columnModel.getColumn(1).setPreferredWidth(150);  // Jenis Peralatan
    columnModel.getColumn(2).setPreferredWidth(100);  // Harga
    columnModel.getColumn(3).setPreferredWidth(150);  // Jumlah Stok
    
    // Sembunyikan kolom ID (kolom ke-5, index = 5)
    jTable1.getColumnModel().getColumn(5).setMinWidth(0);
    jTable1.getColumnModel().getColumn(5).setMaxWidth(0);
    jTable1.getColumnModel().getColumn(5).setWidth(0);


    String sql = "SELECT id_peralatan, nama_peralatan, jenis_peralatan, harga, jumlah_stok FROM inventaris_peralatan";
    try (Connection conn = DbConn.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {
            Object[] row = {
                false,
                rs.getString("nama_peralatan"),
                rs.getString("jenis_peralatan"),
                rs.getBigDecimal("harga"),
                rs.getInt("jumlah_stok"),
                rs.getInt("id_peralatan")
            };
            model.addRow(row);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Gagal memuat data: " + e.getMessage(),
            "Database Error", JOptionPane.ERROR_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        Stok_Barang = new javax.swing.JLabel();
        txtharga = new javax.swing.JLabel();
        labeljenis = new javax.swing.JLabel();
        labelnama = new javax.swing.JLabel();
        Hapus = new java.awt.Button();
        Update = new java.awt.Button();
        Tambah = new java.awt.Button();
        txtNama = new java.awt.TextField();
        txtJenis = new java.awt.TextField();
        txtStok = new java.awt.TextField();
        txtHarga = new java.awt.TextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(51, 153, 255));

        Stok_Barang.setForeground(new java.awt.Color(255, 255, 255));
        Stok_Barang.setText("Stok Barang");

        txtharga.setForeground(new java.awt.Color(255, 255, 255));
        txtharga.setText("Harga Barang");

        labeljenis.setForeground(new java.awt.Color(255, 255, 255));
        labeljenis.setText("Jenis Barang");

        labelnama.setForeground(new java.awt.Color(255, 255, 255));
        labelnama.setText("Nama Barang");

        Hapus.setLabel("Hapus");
        Hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HapusActionPerformed(evt);
            }
        });

        Update.setLabel("Update");
        Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateActionPerformed(evt);
            }
        });

        Tambah.setLabel("Tambah");

        txtNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaActionPerformed(evt);
            }
        });

        txtJenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJenisActionPerformed(evt);
            }
        });

        txtStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStokActionPerformed(evt);
            }
        });

        txtHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHargaActionPerformed(evt);
            }
        });

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setForeground(new java.awt.Color(51, 153, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Peralatan", "Nama Peralatan", "Jenis Peralatan", "Harga", "Jumlah Stok"
            }
        ));
        jTable1.setSelectionBackground(new java.awt.Color(51, 102, 255));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(161, 161, 161)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelnama)
                                    .addComponent(labeljenis))
                                .addGap(70, 70, 70)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStok, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtHarga, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(13, 13, 13))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(Stok_Barang)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(Tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(45, 45, 45)
                                                .addComponent(Update, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(36, 36, 36)
                                        .addComponent(Hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGap(382, 382, 382)
                                        .addComponent(txtharga)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap(42, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelnama)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtharga))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labeljenis)
                    .addComponent(Stok_Barang)
                    .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Tambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Update, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))
        );

        Tambah.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaActionPerformed

    private void txtHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHargaActionPerformed

    private void txtJenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJenisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJenisActionPerformed

    private void txtStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStokActionPerformed

    private void UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UpdateActionPerformed

    private void HapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HapusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HapusActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Frame_Inventaris().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button Hapus;
    private javax.swing.JLabel Stok_Barang;
    private java.awt.Button Tambah;
    private java.awt.Button Update;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel labeljenis;
    private javax.swing.JLabel labelnama;
    private java.awt.TextField txtHarga;
    private java.awt.TextField txtJenis;
    private java.awt.TextField txtNama;
    private java.awt.TextField txtStok;
    private javax.swing.JLabel txtharga;
    // End of variables declaration//GEN-END:variables
}
