package projek;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.sql.*;

public class Ekspor_Laporan {
  public static void exportLaporan() {
        Document document = null;
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/arung_futsal", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pembayaran")) {
            
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Laporan_Keuangan.pdf"));
            document.open();

            document.add(new Paragraph("Laporan Keuangan Arung Futsal"));
            document.add(new Paragraph("======================================"));
            
            while (rs.next()) {
                document.add(new Paragraph("Booking ID: " + rs.getInt("booking_id")));
                document.add(new Paragraph("Jumlah: Rp " + rs.getDouble("amount")));
                document.add(new Paragraph("Metode: " + rs.getString("payment_method")));
                document.add(new Paragraph("Tanggal: " + rs.getDate("payment_date")));
                document.add(new Paragraph("--------------------------------------"));
            }

        } catch (SQLException | DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close(); 
            }
        }
    }

}

