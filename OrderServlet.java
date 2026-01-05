package com.ecommerce.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ecommerce.db.DBConnection;

// INNOVATION: Using Annotations instead of complex web.xml mapping
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Retrieve Data
        String productRaw = request.getParameter("product"); // Format: "Name|Price"
        String quantityStr = request.getParameter("quantity");
        String paymentMethod = request.getParameter("payment");

        // Parsing Product Info
        String[] prodDetails = productRaw.split("\\|");
        String productName = prodDetails[0];
        double price = Double.parseDouble(prodDetails[1]);

        // 2. SERVER-SIDE DATA VALIDATION (Security requirement)
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                response.sendRedirect("error.jsp"); // Redirect on invalid data
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("error.jsp");
            return;
        }

        // 3. Business Logic
        double totalPrice = price * quantity;

        // 4. JDBC Integration
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO orders(product_name, quantity, total_price, payment_method) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, productName);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, totalPrice);
            pstmt.setString(4, paymentMethod);

            int result = pstmt.executeUpdate();
            conn.close();

            if (result > 0) {
                // Pass data to success page
                request.setAttribute("userProduct", productName);
                request.setAttribute("userTotal", totalPrice);
                request.getRequestDispatcher("success.jsp").forward(request, response);
            } else {
                response.sendRedirect("error.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}