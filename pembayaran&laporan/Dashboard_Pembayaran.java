package projek;

import javax.swing.*;
import java.awt.*;

public class Dashboard_Pembayaran extends JFrame {
    public Dashboard_Pembayaran() {
        setTitle("Dashboard Laporan Arung Futsal");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

//         Panel atas: grafik statistik
        JPanel pnlChart = new JPanel(new BorderLayout());
        pnlChart.setPreferredSize(new Dimension(900, 400));
        pnlChart.add(new Statistik_Pendapatan().getContentPane(), BorderLayout.CENTER);
        add(pnlChart, BorderLayout.CENTER);

//         Panel bawah: tombol aksi
        JPanel pnlButtons = new JPanel();
        JButton btnAdd = new JButton("Add Payment");
        JButton btnExport = new JButton("Export Report");
        JButton btnRekap = new JButton("View Summary");

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnExport);
        pnlButtons.add(btnRekap);
        add(pnlButtons, BorderLayout.SOUTH);

//         Aksi tombol
        btnAdd.addActionListener(e -> new Input_Pembayaran().setVisible(true));
        btnExport.addActionListener(e -> Ekspor_Laporan.exportPDF());
        btnRekap.addActionListener(e -> new Rekap_Pendapatan().setVisible(true));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard_Pembayaran().setVisible(true));
    }
}
