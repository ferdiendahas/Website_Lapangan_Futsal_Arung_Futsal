
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
    private JTextField txtId, txtNama, txtJenis, txtHarga, txtStok;

    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Frame_Inventaris.class.getName());

    /**
     * Creates new form Frame_Inventaris
     */
    public Frame_Inventaris() {
        initComponents();
     // Form input
    JLabel lblNama = new JLabel("Nama:");
    txtNama = new JTextField(20);

    JLabel lblJenis = new JLabel("Jenis:");
    txtJenis = new JTextField(20);

    JLabel lblHarga = new JLabel("Harga:");
    txtHarga = new JTextField(10);

    JLabel lblStok = new JLabel("Stok:");
    txtStok = new JTextField(5);

    // ID untuk update
    txtId = new JTextField(5);
    txtId.setVisible(false); // disembunyikan, hanya untuk update

    // Tombol CRUD
    JButton btnTambah = new JButton("Tambah");
    JButton btnUpdate = new JButton("Update");
    JButton btnDelete = new JButton("Hapus");

    // Panel untuk input dan tombol
    JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
    panelForm.add(lblNama); panelForm.add(txtNama);
    panelForm.add(lblJenis); panelForm.add(txtJenis);
    panelForm.add(lblHarga); panelForm.add(txtHarga);
    panelForm.add(lblStok); panelForm.add(txtStok);
    panelForm.add(btnTambah); panelForm.add(btnUpdate);

    // Panel utama
    JPanel panelUtama = new JPanel(new BorderLayout());
    panelUtama.add(panelForm, BorderLayout.NORTH);
    panelUtama.add(new JScrollPane(jTable1), BorderLayout.CENTER);
    panelUtama.add(btnDelete, BorderLayout.SOUTH);

    this.setContentPane(panelUtama);
    this.pack();
    this.setLocationRelativeTo(null);

    loadTableInventaris(); // Panggil fungsi untuk load data ke tabel
    btnTambah.addActionListener(e -> tambahData());
    btnUpdate.addActionListener(e -> updateData());
    btnDelete.addActionListener(e -> hapusData());
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
            Boolean selected = (Boolean) model.getValueAt(i, 0);
            if (selected != null && selected) {
                adaYangDipilih = true;
                int id = (int) model.getValueAt(i, 1);
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
    String[] kolom = {"Select", "Nama Peralatan", "Jenis Peralatan", "Harga", "Jumlah Stok"};
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

    String sql = "SELECT nama_peralatan, jenis_peralatan, harga, jumlah_stok FROM inventaris_peralatan";
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
