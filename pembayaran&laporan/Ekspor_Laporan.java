package projek;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.sql.*;
import javax.swing.JOptionPane;

public class Ekspor_Laporan {
    public static void exportPDF() {
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream("Laporan_Pembayaran.pdf"));
            doc.open();

            doc.add(new Paragraph("Laporan Pembayaran - Arung Futsal"));
            doc.add(new Paragraph(" "));
            
            PdfPTable table = new PdfPTable(4); 
            table.setWidthPercentage(100);
            table.addCell("Booking ID");
            table.addCell("Tanggal");
            table.addCell("Metode");
            table.addCell("Jumlah (Rp)");

            String sql = "SELECT * FROM pembayaran ORDER BY payment_date DESC";

            try (Connection conn = DatabaseConnection.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    int bookingId = rs.getInt("booking_id");
                    Date tanggal = rs.getDate("payment_date");
                    String metode = rs.getString("payment_method");
                    double jumlah = rs.getDouble("amount");

                    table.addCell(String.valueOf(bookingId));
                    table.addCell(String.valueOf(tanggal));
                    table.addCell(metode);
                    table.addCell("Rp " + String.format("%.0f", jumlah));
                }
            }

            doc.add(table);
            doc.close();

            JOptionPane.showMessageDialog(null, "Laporan lengkap berhasil diekspor ke Laporan_Pembayaran.pdf");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengekspor laporan: " + e.getMessage());
        }
    }
}
