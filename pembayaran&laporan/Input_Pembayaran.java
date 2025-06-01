package projek;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Input_Pembayaran extends JFrame {
    private JTextField txtBookingId, txtAmount;
    private JComboBox<String> cbMethod;
    private JButton btnPay;

    public Input_Pembayaran() {
        setTitle("Input Pembayaran");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; add(new JLabel("Booking ID:"), c);
        txtBookingId = new JTextField(); c.gridx = 1; add(txtBookingId, c);

        c.gridx = 0; c.gridy = 1; add(new JLabel("Metode:"), c);
        cbMethod = new JComboBox<>(new String[]{"Tunai","Transfer"}); c.gridx = 1; add(cbMethod, c);

        c.gridx = 0; c.gridy = 2; add(new JLabel("Jumlah (Rp):"), c);
        txtAmount = new JTextField(); c.gridx = 1; add(txtAmount, c);

        btnPay = new JButton("Bayar");
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        add(btnPay, c);

        btnPay.addActionListener(e -> prosesBayar());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void prosesBayar() {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement p1 = conn.prepareStatement(
                   "INSERT INTO pembayaran (booking_id, payment_date, payment_method, amount) VALUES (?, NOW(), ?, ?)");
             PreparedStatement p2 = conn.prepareStatement(
                   "UPDATE booking SET status='Lunas' WHERE booking_id=?")) {

            int id = Integer.parseInt(txtBookingId.getText());
            double amt = Double.parseDouble(txtAmount.getText());
            String m = cbMethod.getSelectedItem().toString();

            p1.setInt(1, id);
            p1.setString(2, m);
            p1.setDouble(3, amt);
            p1.executeUpdate();

            p2.setInt(1, id);
            p2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Pembayaran berhasil!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage());
            ex.printStackTrace();
        }
    }

    
}
