public class Main {
    public static void main(String[] args) {
        // Setup tema FlatLaf
        try {
            com.formdev.flatlaf.FlatIntelliJLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tampilkan login
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
