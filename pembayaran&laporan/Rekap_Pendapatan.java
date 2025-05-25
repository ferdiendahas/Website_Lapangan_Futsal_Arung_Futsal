package projek;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Rekap_Pendapatan {
    public static void tampilkanLaporan() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/arung_futsal", "root", "");
             Statement stmt = conn.createStatement()) {

            // Ambil total pendapatan per bulan
            String query = "SELECT MONTH(payment_date) AS bulan, SUM(amount) AS total " +
                           "FROM pembayaran GROUP BY MONTH(payment_date) ORDER BY bulan";
            ResultSet rs = stmt.executeQuery(query);

            // Peta nama bulan
            Map<Integer, String> bulanMap = new HashMap<>();
            bulanMap.put(1, "Januari");
            bulanMap.put(2, "Februari");
            bulanMap.put(3, "Maret");
            bulanMap.put(4, "April");
            bulanMap.put(5, "Mei");
            bulanMap.put(6, "Juni");
            bulanMap.put(7, "Juli");
            bulanMap.put(8, "Agustus");
            bulanMap.put(9, "September");
            bulanMap.put(10, "Oktober");
            bulanMap.put(11, "November");
            bulanMap.put(12, "Desember");

            System.out.println("=== Rekap Pendapatan Arung Futsal ===");
            while (rs.next()) {
                int bulan = rs.getInt("bulan");
                double total = rs.getDouble("total");
                String namaBulan = bulanMap.getOrDefault(bulan, "Bulan Tidak Diketahui");
                System.out.printf("%-12s : Rp %.0f\n", namaBulan, total);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
  

