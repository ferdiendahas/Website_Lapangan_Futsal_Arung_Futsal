package projek;

import javax.swing.*;
import java.sql.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;

public class Statistik_Pendapatan extends JFrame {

    public Statistik_Pendapatan() {
        setTitle("Statistik Pendapatan Bulanan");
        setSize(800, 500);
        setLocationRelativeTo(null);

        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        String sql = "SELECT MONTH(payment_date) AS bl, SUM(amount) AS tot "
                   + "FROM pembayaran GROUP BY bl ORDER BY bl";
        String[] nama = {"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Agu","Sep","Okt","Nov","Des"};

        try (Connection c = DatabaseConnection.connect();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(sql)) {
            while (r.next()) {
                int b = r.getInt("bl");
                double t = r.getDouble("tot");
                ds.addValue(t, "Pendapatan", nama[b-1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Pendapatan Bulanan", "Bulan", "Rp", ds,
            PlotOrientation.VERTICAL, false, true, false);
        ChartPanel cp = new ChartPanel(chart);
        setContentPane(cp);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

 
}
