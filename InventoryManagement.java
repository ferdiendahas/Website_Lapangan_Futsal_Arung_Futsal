import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class InventoryItem {
    private String name;
    private String type;
    private double price;
    private int stock;

    public InventoryItem(String name, String type, double price, int stock) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

public class InventoryManagement extends JPanel {
    private JTextField tfName, tfType, tfPrice, tfStock;
    private JTable table;
    private DefaultTableModel tableModel;
    private ArrayList<InventoryItem> inventoryItems;

    public InventoryManagement() {
        inventoryItems = new ArrayList<>();
        setLayout(new BorderLayout());

        // Create input panel with GridBagLayout for better control
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Nama Barang:"), gbc);
        tfName = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfName, gbc);

        // Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Jenis:"), gbc);
        tfType = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfType, gbc);

        // Price
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Harga:"), gbc);
        tfPrice = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfPrice, gbc);

        // Stock
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Stok:"), gbc);
        tfStock = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfStock, gbc);

        // Buttons
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(btnAdd, gbc);
        gbc.gridx = 1;
        inputPanel.add(btnUpdate, gbc);
        gbc.gridx = 2;
        inputPanel.add(btnDelete, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Create table
        String[] columnNames = {"Nama Barang", "Jenis", "Harga", "Stok"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Button actions
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateItem();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteItem();
            }
        });
    }

    private void addItem() {
        String name = tfName.getText();
        String type = tfType.getText();
        double price;
        int stock;

        try {
            price = Double.parseDouble(tfPrice.getText());
            stock = Integer.parseInt(tfStock.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price and Stock must be numbers.");
            return;
        }

        InventoryItem item = new InventoryItem(name, type, price, stock);
        inventoryItems.add(item);
        tableModel.addRow(new Object[]{name, type, price, stock});
        clearFields();
    }

    private void updateItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to update.");
            return;
        }

        String name = tfName.getText();
        String type = tfType.getText();
        double price;
        int stock;

        try {
            price = Double.parseDouble(tfPrice.getText());
            stock = Integer.parseInt(tfStock.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price and Stock must be numbers.");
            return;
        }

        InventoryItem item = inventoryItems.get(selectedRow);
        item.setName(name);
        item.setType(type);
        item.setPrice(price);
        item.setStock(stock);

        tableModel.setValueAt(name, selectedRow, 0);
        tableModel.setValueAt(type, selectedRow, 1);
        tableModel.setValueAt(price, selectedRow, 2);
        tableModel.setValueAt(stock, selectedRow, 3);
        clearFields();
    }

    private void deleteItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to delete.");
            return;
        }

        inventoryItems.remove(selectedRow);
        tableModel.removeRow(selectedRow);
        clearFields();
    }

    private void clearFields() {
        tfName.setText("");
        tfType.setText("");
        tfPrice.setText("");
        tfStock.setText("");
    }
}
