package projek;
import java.sql.*;
public class Rekap_Pendapatan {
     public static void tampilkanLaporan() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/arung_futsal", "root", "");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT SUM(amount) AS total FROM pembayaran");
            if (rs.next()) {
                System.out.println("Total Pendapatan: Rp " + rs.getDouble("total"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
