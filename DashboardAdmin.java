/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package arungfutsall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class DashboardAdmin extends javax.swing.JFrame {
    private JPanel panelContent;
    private CardLayout cardLayout;
    public PanelManajemenLapangan panelManajemenLapangan;
    public PanelBooking panelBooking;
    public PanelLaporan panelLaporan;
    public PanelInventaris panelInventaris;

    private final Color sidebarColor = new Color(22, 32, 60);
    private final Color buttonBg = new Color(30, 56, 110);
    private final Color accentColor = new Color(52, 167, 240);
    private final Color selectedBg = new Color(255, 215, 0); // KUNING untuk menu aktif
    private final Color glossy = new Color(38, 74, 140, 190);

    private int activeMenuIndex = -1; // Simpan menu yang aktif

    public DashboardAdmin() {
        setTitle("⚽ Dashboard Admin - Arung Futsal");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // LOGO PANEL di atas sidebar
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(34, 48, 85), 0, getHeight(), new Color(22, 32, 60)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
        logoPanel.setLayout(new GridBagLayout());
        logoPanel.setPreferredSize(new Dimension(80, 100));
        logoPanel.setOpaque(false);
        JLabel lblLogo = new JLabel("<html><span style='color:#FFF;font-size:26px;font-family:Arial Black'>ARUNG</span><span style='color:#FDC900;font-size:26px;font-family:Arial Black'> FUTSAL</span></html>");
        lblLogo.setFont(new Font("Arial Black", Font.BOLD, 26));
        logoPanel.add(lblLogo);

        // SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(sidebarColor);
        sidebar.setPreferredSize(new Dimension(220, getHeight()));

        String[][] menuData = {
            {"\uD83D\uDCC5", "BOOKING", "Booking"},               // 📅
            {"\u26BD", "MANAJEMEN", "Manajemen Lapangan"},        // ⚽
            {"\uD83D\uDCC8", "LAPORAN", "Laporan Pembayaran"},    // 📈
            {"\uD83D\uDEE0", "INVENTARIS", "Inventaris"}          // 🛠️
        };

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(sidebarColor);
        menuPanel.setLayout(new GridLayout(menuData.length, 1, 0, 12));

        JButton[] menuButtons = new JButton[menuData.length];

        for (int i = 0; i < menuData.length; i++) {
            menuButtons[i] = new JButton(menuData[i][0] + "  " + menuData[i][1]);
            menuButtons[i].setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
            menuButtons[i].setBackground(buttonBg);
            menuButtons[i].setForeground(Color.WHITE);
            menuButtons[i].setHorizontalAlignment(SwingConstants.LEFT);
            menuButtons[i].setBorder(new CompoundBorder(
                new LineBorder(new Color(18, 29, 54), 2, true),
                new EmptyBorder(12, 18, 12, 6)
            ));
            menuButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            menuButtons[i].setFocusPainted(false);
            menuButtons[i].setPreferredSize(new Dimension(50, 18));
            int finalI = i;
            menuButtons[i].addActionListener(e -> {
                setActiveMenu(finalI, menuButtons); // highlight menu
                showCard(menuData[finalI][2]);
            });
            menuPanel.add(menuButtons[i]);
        }
        setActiveMenu(0, menuButtons); // Jadikan menu pertama aktif saat start

        JButton btnExit = new JButton("EXIT");
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnExit.setBackground(new Color(190,38,38));
        btnExit.setForeground(Color.WHITE);
        btnExit.setBorder(new CompoundBorder(
            new LineBorder(Color.WHITE, 2, true),
            new EmptyBorder(8, 20, 8, 6)
        ));
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.setFocusPainted(false);
        btnExit.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(this, "Tutup aplikasi?", "Exit", JOptionPane.YES_NO_OPTION);
            if(conf==JOptionPane.YES_OPTION) System.exit(0);
        });

        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(btnExit, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);

        // Panel konten (utama)
        panelContent = new JPanel();
        cardLayout = new CardLayout();
        panelContent.setLayout(cardLayout);

        panelManajemenLapangan = new PanelManajemenLapangan();
        panelBooking = new PanelBooking();
        panelLaporan = new PanelLaporan();
        panelInventaris = new PanelInventaris();

        panelContent.add(panelBooking, "Booking");
        panelContent.add(panelManajemenLapangan, "Manajemen Lapangan");
        panelContent.add(panelLaporan, "Laporan Pembayaran");
        panelContent.add(panelInventaris, "Inventaris");

        add(sidebar, BorderLayout.WEST);
        add(panelContent, BorderLayout.CENTER);
    }

    /** 
     * Fungsi untuk highlight tombol sidebar yang aktif
     */
    private void setActiveMenu(int selectedIdx, JButton[] menuButtons) {
        Color activeColor = selectedBg; // kuning
        for (int i = 0; i < menuButtons.length; i++) {
            if (i == selectedIdx) {
                menuButtons[i].setBackground(activeColor);
                menuButtons[i].setForeground(new Color(44, 84, 163)); // Biru tua
                menuButtons[i].setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
            } else {
                menuButtons[i].setBackground(buttonBg);
                menuButtons[i].setForeground(Color.WHITE);
                menuButtons[i].setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
            }
        }
        activeMenuIndex = selectedIdx;
    }

    private void showCard(String name) {
        cardLayout.show(panelContent, name);
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardAdmin().setVisible(true));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
