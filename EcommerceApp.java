
// GUI imports
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;

// Collections (Fixes List ambiguity)
import java.util.List;
import java.util.ArrayList;

// SQL imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// -------------------------------------------------------------
// INTERFACE (OOP Requirement: Interfaces & Polymorphism)
// -------------------------------------------------------------
interface Payment {
    void pay(double amount);
}

// -------------------------------------------------------------
// POLYMORPHISM (Multiple payment approaches)
// -------------------------------------------------------------
class UpiPayment implements Payment {
    public void pay(double amount) {
        JOptionPane.showMessageDialog(null, "💳 Payment Successful using UPI: ₹" + amount);
    }
}

class CardPayment implements Payment {
    public void pay(double amount) {
        JOptionPane.showMessageDialog(null, "💳 Payment Successful using Card: ₹" + amount);
    }
}

// -------------------------------------------------------------
// ENCAPSULATION (Private attributes with getters)
// -------------------------------------------------------------
class Product {
    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String toString() {
        return id + ". " + name + " - ₹" + price;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}

// -------------------------------------------------------------
// COLLECTIONS & GENERICS (Cart implementation)
// -------------------------------------------------------------
class Cart<T> {

    List<T> items = new ArrayList<>();

    public void addItem(T item) {
        items.add(item);
    }

    public String displayCart() {
        if (items.isEmpty())
            return "Cart is empty!";

        StringBuilder sb = new StringBuilder("🛒 CART ITEMS:\n");
        for (T item : items)
            sb.append(item).append("\n");
        return sb.toString();
    }

    public T getFirstItem() {
        return items.get(0);
    }
}

// -------------------------------------------------------------
// MULTITHREADING + SYNCHRONIZATION (Order Processing Simulation)
// -------------------------------------------------------------
class OrderProcessor extends Thread {

    synchronized public void run() {
        try {
            JOptionPane.showMessageDialog(null, "Processing order... please wait ⏳");
            Thread.sleep(2000);
            JOptionPane.showMessageDialog(null, "🚚 Order Confirmed & Shipping Soon!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Thread Error: " + e.getMessage());
        }
    }
}

// -------------------------------------------------------------
// JDBC SEPARATE CLASS (Database Operation Class)
// -------------------------------------------------------------
class DatabaseOperations {

    private Connection conn;

    // Connect to MySQL Database
    public void connect() throws Exception {
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ecommerce_db",
                "root",
                "king234");
        System.out.println("Database Connected...");
    }

    // Insert order details into table
    public void saveOrder(int productId, double amount, String paymentMethod) throws Exception {
        String query = "INSERT INTO orders(product_id, quantity, total_price, payment_method) VALUES (?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, productId);
        stmt.setInt(2, 1);
        stmt.setDouble(3, amount);
        stmt.setString(4, paymentMethod);

        stmt.executeUpdate();
        JOptionPane.showMessageDialog(null, "Order Saved in Database Successfully");
    }

    // Close DB connection
    public void close() throws Exception {
        conn.close();
    }
}

// -------------------------------------------------------------
// MAIN APPLICATION WITH GRAPHICAL USER INTERFACE
// -------------------------------------------------------------
public class EcommerceApp {

    public static void main(String[] args) {

        Product p1 = new Product(1, "Laptop", 55000);
        Product p2 = new Product(2, "Smart Watch", 2500);
        Product p3 = new Product(3, "Earbuds", 1500);

        Cart<Product> cart = new Cart<>();

        JFrame frame = new JFrame("🛍️ E-Commerce Store");
        frame.setSize(420, 450);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Select a Product to Add to Cart:");
        title.setBounds(80, 20, 260, 20);
        frame.add(title);

        JComboBox<String> productDropdown = new JComboBox<>(new String[] {
                p1.toString(), p2.toString(), p3.toString()
        });
        productDropdown.setBounds(80, 60, 260, 30);
        frame.add(productDropdown);

        JButton addBtn = new JButton("Add to Cart");
        addBtn.setBounds(130, 110, 150, 30);
        frame.add(addBtn);

        JTextArea cartArea = new JTextArea();
        cartArea.setBounds(60, 160, 290, 120);
        cartArea.setEditable(false);
        frame.add(cartArea);

        JButton payBtn = new JButton("Proceed to Payment");
        payBtn.setBounds(120, 300, 180, 30);
        payBtn.setEnabled(false);
        frame.add(payBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBounds(160, 350, 100, 30);
        frame.add(exitBtn);

        addBtn.addActionListener(e -> {
            int choice = productDropdown.getSelectedIndex();

            Product selected = switch (choice) {
                case 0 -> p1;
                case 1 -> p2;
                default -> p3;
            };

            cart.addItem(selected);
            cartArea.setText(cart.displayCart());

            payBtn.setEnabled(true);
        });

        payBtn.addActionListener(e -> {
            Product selectedProduct = cart.getFirstItem();
            double amount = selectedProduct.getPrice();

            String[] paymentOptions = { "UPI", "Card" };
            int selectedMethod = JOptionPane.showOptionDialog(null,
                    "Choose Payment Method:",
                    "Payment", 0, 0, null, paymentOptions, null);

            Payment payment = (selectedMethod == 0) ? new UpiPayment() : new CardPayment();
            payment.pay(amount);

            new OrderProcessor().start();

            try {
                DatabaseOperations db = new DatabaseOperations();
                db.connect();
                db.saveOrder(selectedProduct.getId(), amount,
                        (selectedMethod == 0 ? "UPI" : "CARD"));
                db.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
            }

            JOptionPane.showMessageDialog(null, "🎉 THANK YOU FOR SHOPPING 🎉");
        });

        exitBtn.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }
}
