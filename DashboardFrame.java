import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private JLabel contentTitleLabel;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public DashboardFrame() {
        setTitle("Dashboard Admin - Arung Futsal");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Dashboard Admin - Arung Futsal", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        header.setOpaque(true);
        header.setBackground(new Color(45, 118, 232));
        header.setForeground(Color.WHITE);
        add(header, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = new JPanel(new GridLayout(4, 1, 15, 15));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setBackground(new Color(37, 56, 88));
        sidebar.add(createNavButton("Manajemen Lapangan"));
        sidebar.add(createNavButton("Manajemen Pelanggan"));
        sidebar.add(createNavButton("Booking"));
        sidebar.add(createNavButton("Inventaris"));
        add(sidebar, BorderLayout.WEST);

        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentTitleLabel = new JLabel("Selamat Datang di Dashboard", SwingConstants.LEFT);
        contentTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        contentTitleLabel.setForeground(new Color(45, 118, 232));
        contentTitleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        contentPanel.add(contentTitleLabel, BorderLayout.NORTH);

        // Card Layout for different sections
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(new JLabel("Pilih menu dari navigasi di kiri", SwingConstants.CENTER), "home");
        cardPanel.add(new FormBooking(), "booking");
        cardPanel.add(new InventoryManagement(), "inventaris"); // Add Inventory Management panel
        contentPanel.add(cardPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String label) {
        JButton btn = new JButton(label);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBackground(new Color(68, 138, 255));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> updateContent(label));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(40, 100, 210));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(68, 138, 255));
            }
        });
        return btn;
    }

    private void updateContent(String sectionTitle) {
        contentTitleLabel.setText("Halaman " + sectionTitle);
        if (sectionTitle.equals("Booking")) {
            cardLayout.show(cardPanel, "booking");
        } else if (sectionTitle.equals("Inventaris")) {
            cardLayout.show(cardPanel, "inventaris");
        } else {
            cardLayout.show(cardPanel, "home");
        }
    }
}
