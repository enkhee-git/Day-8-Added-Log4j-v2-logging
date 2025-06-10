package sales;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class SalesSystemGUI extends JFrame {
    private static final Logger logger = LogManager.getLogger(SalesSystemGUI.class);

    private List<Product> products = new ArrayList<>();
    private Customer customer;
    private Order order;
    private JTextArea orderArea;
    private JComboBox<String> productCombo;
    private JTextField qtyField;

    public SalesSystemGUI() {
        try {
            // Ensure logs directory exists
            logger.info("Application starting...");

            setTitle("Sales System");
            setSize(500, 400);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            initProducts();
            showCustomerDialog();
            order = new Order(customer);
            logger.info("New order created for customer: {}", customer.getName());

            setupUI();
            logger.info("UI setup completed");
        } catch (Exception e) {
            logger.error("Error initializing application", e);
            JOptionPane.showMessageDialog(this, "Failed to initialize application", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initProducts() {
        try {
            products.add(new Product("Laptop", 2500000, 10));
            products.add(new Product("Mouse", 25000, 50));
            products.add(new Product("Keyboard", 60000, 30));
            logger.info("Initialized {} products", products.size());
        } catch (IllegalArgumentException e) {
            logger.error("Error initializing products", e);
            throw e;
        }
    }

    private void showCustomerDialog() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Phone:", phoneField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Customer Registration",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            try {
                customer = new Customer(name, email, phone);
                logger.info("New customer registered: {}", name);
            } catch (IllegalArgumentException ex) {
                logger.error("Invalid customer data - Name: {}, Email: {}, Phone: {}", name, email, phone, ex);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                showCustomerDialog(); // Recursive call to retry
            }
        } else {
            logger.info("User canceled customer registration");
            System.exit(0);
        }
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        productCombo = new JComboBox<>();
        updateProductCombo();

        qtyField = new JTextField(5);

        JButton addButton = new JButton("Order");
        addButton.addActionListener(e -> addOrder());

        topPanel.add(new JLabel("Select product:"));
        topPanel.add(productCombo);
        topPanel.add(new JLabel("Quantity:"));
        topPanel.add(qtyField);
        topPanel.add(addButton);

        add(topPanel, BorderLayout.NORTH);

        orderArea = new JTextArea();
        orderArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addOrder() {
        int index = productCombo.getSelectedIndex();
        Product selected = products.get(index);
        String qtyText = qtyField.getText().trim();

        if (qtyText.isEmpty()) {
            logger.warn("Attempted to add order with empty quantity");
            JOptionPane.showMessageDialog(this, "Please enter quantity!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText);
            if (qty <= 0) {
                logger.warn("Invalid quantity entered: {}", qtyText);
                throw new NumberFormatException();
            }

            if (order.addItem(selected, qty)) {
                logger.info("Added {} units of {} to order", qty, selected.getName());
                updateOrderArea();
                updateProductCombo();
                qtyField.setText("");
                JOptionPane.showMessageDialog(this, "Order successfully added!");
            } else {
                logger.warn("Insufficient stock for {} (requested: {}, available: {})",
                        selected.getName(), qty, selected.getStockQuantity());
                JOptionPane.showMessageDialog(this,
                        "Insufficient stock!\nAvailable: " + selected.getStockQuantity(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            logger.warn("Invalid quantity format: {}", qtyText);
            JOptionPane.showMessageDialog(this, "Quantity must be a positive number!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            logger.error("Error adding order item", ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateOrderArea() {
        orderArea.setText(order.getOrderDetails());
        logger.debug("Updated order display");
    }

    private void updateProductCombo() {
        productCombo.removeAllItems();
        for (Product p : products) {
            productCombo.addItem(p.toString());
        }
        logger.debug("Updated product combo box");
    }

    public static void main(String[] args) {
        // Set log directory before initializing Log4j
        System.setProperty("logDir", "logs");

        SwingUtilities.invokeLater(() -> {
            try {
                new SalesSystemGUI().setVisible(true);
            } catch (Exception e) {
                logger.error("Fatal error in application", e);
                JOptionPane.showMessageDialog(null,
                        "A fatal error occurred. Please check logs.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}