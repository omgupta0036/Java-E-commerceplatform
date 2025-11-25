import java.util.*;
import java.sql.*;

// ---------------------- INTERFACE ----------------------
interface Payment {
    void pay(double amount);
}

// ---------------------- POLYMORPHISM --------------------
class UpiPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Payment Successful: ₹" + amount + " paid using UPI.");
    }
}

class CardPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Payment Successful: ₹" + amount + " paid using Card.");
    }
}

// ---------------------- ENCAPSULATION --------------------
class Product {
    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String toString(){
        return id + " | " + name + " | ₹" + price;
    }

    public double getPrice(){ return price; }
    public String getName(){ return name; }
}

// ------------------- COLLECTIONS + GENERICS ----------------
class Cart<T> {
    List<T> items = new ArrayList<>();

    public void addItem(T item){
        items.add(item);
    }

    public void showCart(){
        if(items.isEmpty()){
            System.out.println("Cart is Empty!");
        } else {
            System.out.println("\n🛒 CART ITEMS:");
            for(T item : items){
                System.out.println(item);
            }
        }
    }
}

// ---------------- MULTITHREADING + SYNCHRONIZATION ----------
class OrderProcessor extends Thread {
    synchronized public void run() {
        System.out.println("\nProcessing Order...");
        try { Thread.sleep(2000); } catch (Exception e) {}
        System.out.println("Order Placed Successfully! 🚚");
    }
}

// ---------------- DATABASE OPERATIONS (JDBC) ----------------
class DatabaseOperations {

    private Connection conn;

    public void connect() throws Exception {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce","root","yourpassword");
        System.out.println("\nDatabase Connected Successfully!");
    }

    public void saveOrder(String product, double amount) throws Exception {
        String query = "INSERT INTO orders(product_name,amount) VALUES(?,?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, product);
        stmt.setDouble(2, amount);
        stmt.executeUpdate();
        System.out.println("Order Saved in Database.");
    }

    public void close() throws Exception {
        conn.close();
    }
}

// ---------------------- MAIN APPLICATION ----------------------
 class EcommerceApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Available Products
        Product p1 = new Product(1, "Laptop", 55000);
        Product p2 = new Product(2, "Smart Watch", 2500);
        Product p3 = new Product(3, "Earbuds", 1500);

        Cart<Product> cart = new Cart<>();

        System.out.println("\n===== 🛍️ ONLINE E-COMMERCE STORE 🛍️ =====");
        System.out.println("\nAvailable Products:");
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);

        System.out.print("\nEnter Product ID to Add to Cart: ");
        int choice = sc.nextInt();

        try {
            switch(choice) {
                case 1 -> cart.addItem(p1);
                case 2 -> cart.addItem(p2);
                case 3 -> cart.addItem(p3);
                default -> throw new Exception("Invalid Product Selection!");
            }
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        cart.showCart();

        double billAmount = (choice == 1 ? p1.getPrice() : choice == 2 ? p2.getPrice() : p3.getPrice());

        System.out.println("\nSelect Payment Method:");
        System.out.println("1. UPI");
        System.out.println("2. Card");
        System.out.print("Enter choice: ");
        int payChoice = sc.nextInt();

        Payment payment;
        payment = (payChoice == 1) ? new UpiPayment() : new CardPayment();
        payment.pay(billAmount);

        // Multithread Simulation
        OrderProcessor processor = new OrderProcessor();
        processor.start();

        // JDBC SAVE
        try {
            DatabaseOperations db = new DatabaseOperations();
            db.connect();
            db.saveOrder(cart.items.get(0).getName(), billAmount);
            db.close();
        } catch(Exception e){
            System.out.println("Database Error: " + e.getMessage());
        }

        System.out.println("\nThank you for shopping with us! 💙");
        sc.close();
    }
}
