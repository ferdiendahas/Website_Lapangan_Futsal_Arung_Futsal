package projek;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class Rekap_Pendapatan extends JFrame {
    private JTextArea area;

    public Rekap_Pendapatan() {
        setTitle("Rekap Pendapatan Bulanan");
        setSize(400, 450);
        setLocationRelativeTo(null);
        area = new JTextArea();
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        area.setEditable(false);
        add(new JScrollPane(area));

        tampilkan();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void tampilkan() {
        Map<Integer,String> mp = Map.ofEntries(
            Map.entry(1,"Januari"),Map.entry(2,"Februari"),Map.entry(3,"Maret"),
            Map.entry(4,"April"),Map.entry(5,"Mei"),Map.entry(6,"Juni"),
            Map.entry(7,"Juli"),Map.entry(8,"Agustus"),Map.entry(9,"September"),
            Map.entry(10,"Oktober"),Map.entry(11,"November"),Map.entry(12,"Desember")
        );
        StringBuilder sb = new StringBuilder("Rekap Pendapatan Bulanan\nPenyewaan Lapangan Arung Futsal\n\n");
        String sql = "SELECT MONTH(payment_date) AS bln, SUM(amount) AS total "
                   + "FROM pembayaran GROUP BY bln ORDER BY bln";
        try (Connection c = DatabaseConnection.connect();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(sql)) {
            while (r.next()) {
                int b = r.getInt("bln");
                double t = r.getDouble("total");
                sb.append(String.format("%-10s : Rp %.0f\n", mp.get(b), t));
            }
        } catch (Exception e) {
            sb.append("Gagal: ").append(e.getMessage());
            e.printStackTrace();
        }
        area.setText(sb.toString());
    }
}
