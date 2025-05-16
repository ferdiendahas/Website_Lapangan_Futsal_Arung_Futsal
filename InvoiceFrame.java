package PenjadwalanAndBooking;

import javax.swing.*;

public class InvoiceFrame extends JFrame {
    public InvoiceFrame(DataBooking db) {
        setTitle("Invoice");
        setSize(400, 250);
        setLayout(null);

        JTextArea area = new JTextArea();
        area.setBounds(20, 20, 340, 160);
        area.setEditable(false);

        String invoice = "===== INVOICE BOOKING =====\n";
        invoice += "Nama      : " + db.nama + "\n";
        invoice += "Lapangan  : " + db.lapangan + "\n";
        invoice += "Tanggal   : " + db.tanggal + "\n";
        invoice += "Jam       : " + db.jam + "\n";
        invoice += "Durasi    : " + db.durasi + " jam\n";
        invoice += "Total Bayar : Rp " + db.hitungTotalBayar() + "\n";
        invoice += "===========================";

        area.setText(invoice);
        add(area);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

