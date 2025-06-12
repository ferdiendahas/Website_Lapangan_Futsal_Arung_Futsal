import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FutsalAdminGUI extends JFrame {
    JTextField tfNama, tfNoHP, tfTanggal;
    JComboBox<String> cbLapangan, cbJamMulai, cbJamSelesai;
    JTable table;
    DefaultTableModel tableModel;
    Connection conn;

    public FutsalAdminGUI() {
        // Koneksi Database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/futsal_db", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal koneksi ke database.");
            System.exit(1);
        }

        setTitle("Admin Persewaan Futsal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel Input
        JPanel panelInput = new JPanel(new GridLayout(6, 2, 10, 10));
        tfNama = new JTextField();
        tfNoHP = new JTextField();
        tfTanggal = new JTextField("yyyy-mm-dd");
        cbLapangan = new JComboBox<>(new String[]{"A", "B", "C"});
        cbJamMulai = new JComboBox<>(generateJamMulai());
        cbJamSelesai = new JComboBox<>(generateJamSelesai());

        panelInput.add(new JLabel("Nama Pelanggan:"));
        panelInput.add(tfNama);
        panelInput.add(new JLabel("Lapangan:"));
        panelInput.add(cbLapangan);
        panelInput.add(new JLabel("Tanggal (yyyy-mm-dd):"));
        panelInput.add(tfTanggal);
        panelInput.add(new JLabel("Jam Mulai:"));
        panelInput.add(cbJamMulai);
        panelInput.add(new JLabel("Jam Selesai:"));
        panelInput.add(cbJamSelesai);
        panelInput.add(new JLabel("No HP:"));
        panelInput.add(tfNoHP);

        add(panelInput, BorderLayout.NORTH);

        // Tabel
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Lapangan", "Tanggal", "Mulai", "Selesai", "HP"}, 0);
        table = new JTable(tableModel);
        loadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel Tombol
        JPanel panelButton = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        panelButton.add(btnTambah);
        panelButton.add(btnEdit);
        panelButton.add(btnHapus);
        add(panelButton, BorderLayout.SOUTH);

        // Tambah Data
        btnTambah.addActionListener(e -> tambahData());

        // Hapus Data
        btnHapus.addActionListener(e -> hapusData());

        // Edit Data
        btnEdit.addActionListener(e -> editData());

        // Klik Tabel
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    tfNama.setText(tableModel.getValueAt(row, 1).toString());
                    cbLapangan.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    tfTanggal.setText(tableModel.getValueAt(row, 3).toString());
                    cbJamMulai.setSelectedItem(tableModel.getValueAt(row, 4).toString());
                    cbJamSelesai.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                    tfNoHP.setText(tableModel.getValueAt(row, 6).toString());
                }
            }
        });

        setVisible(true);
    }

    private void tambahData() {
        String nama = tfNama.getText();
        String lapangan = cbLapangan.getSelectedItem().toString();
        String tanggal = tfTanggal.getText();
        String jamMulai = cbJamMulai.getSelectedItem().toString();
        String jamSelesai = cbJamSelesai.getSelectedItem().toString();
        String noHp = tfNoHP.getText();

        if (isWaktuTabrakan(tanggal, lapangan, jamMulai, jamSelesai, -1)) {
            JOptionPane.showMessageDialog(this, "Jadwal bentrok! Sudah ada penyewaan di waktu tersebut untuk Lapangan " + lapangan);
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO penyewaan (nama_pelanggan, lapangan, tanggal, jam_mulai, jam_selesai, no_hp) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, nama);
            ps.setString(2, lapangan);
            ps.setString(3, tanggal);
            ps.setString(4, jamMulai);
            ps.setString(5, jamSelesai);
            ps.setString(6, noHp);
            ps.executeUpdate();
            loadTable();
            clearForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            try {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM penyewaan WHERE id = ?");
                ps.setInt(1, id);
                ps.executeUpdate();
                loadTable();
                clearForm();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void editData() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            String nama = tfNama.getText();
            String lapangan = cbLapangan.getSelectedItem().toString();
            String tanggal = tfTanggal.getText();
            String jamMulai = cbJamMulai.getSelectedItem().toString();
            String jamSelesai = cbJamSelesai.getSelectedItem().toString();
            String noHp = tfNoHP.getText();

            if (isWaktuTabrakan(tanggal, lapangan, jamMulai, jamSelesai, id)) {
                JOptionPane.showMessageDialog(this, "Jadwal bentrok! Sudah ada penyewaan lain di waktu tersebut.");
                return;
            }

            try {
                PreparedStatement ps = conn.prepareStatement("UPDATE penyewaan SET nama_pelanggan=?, lapangan=?, tanggal=?, jam_mulai=?, jam_selesai=?, no_hp=? WHERE id=?");
                ps.setString(1, nama);
                ps.setString(2, lapangan);
                ps.setString(3, tanggal);
                ps.setString(4, jamMulai);
                ps.setString(5, jamSelesai);
                ps.setString(6, noHp);
                ps.setInt(7, id);
                ps.executeUpdate();
                loadTable();
                clearForm();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
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

    public static void main(String[] args) {
        // Optional: set look and feel to Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}
        new FutsalAdminGUI();
    }
}
