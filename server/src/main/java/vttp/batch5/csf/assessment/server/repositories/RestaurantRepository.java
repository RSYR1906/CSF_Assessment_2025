package vttp.batch5.csf.assessment.server.repositories;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Authenticate user against customers table
    public boolean authenticateUser(String username, String password) {
        try {
            String sql = "SELECT COUNT(*) FROM customers WHERE username = ? AND password = SHA2(?, 224)";

            // Debug logs (remove in production)
            System.out.println("Auth SQL: " + sql);
            System.out.println(
                    "Auth params: username=" + username + ", password=" + (password != null ? "[provided]" : "[null]"));

            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
            System.out.println("Auth result count: " + count);

            return count != null && count > 0;
        } catch (Exception e) {
            System.err.println("Authentication error for user " + username + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Save order to the place_orders table with provided order ID and payment ID
    public void saveOrder(String username, String orderId, String paymentId, double total) {
        try {
            LocalDate orderDate = LocalDate.now();

            String sql = "INSERT INTO place_orders (order_id, payment_id, order_date, total, username) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, orderId, paymentId, orderDate, total, username);

            System.out.println("Order saved successfully: ID=" + orderId + ", Payment=" + paymentId);
        } catch (Exception e) {
            System.err.println("Error saving order for user " + username + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    // Legacy method - keeps backward compatibility
    public String saveOrder(String username, double total) {
        try {
            // Generate unique IDs
            String orderId = generateId(8);
            String paymentId = UUID.randomUUID().toString();
            LocalDate orderDate = LocalDate.now();

            String sql = "INSERT INTO place_orders (order_id, payment_id, order_date, total, username) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, orderId, paymentId, orderDate, total, username);

            System.out.println("Order saved successfully: ID=" + orderId + ", Payment=" + paymentId);
            return orderId + ":" + paymentId;
        } catch (Exception e) {
            System.err.println("Error saving order for user " + username + ": " + e.getMessage());
            e.printStackTrace();
            return "ERROR:" + e.getMessage();
        }
    }

    // Helper method to generate random ID of specified length
    private String generateId(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (chars.length() * Math.random());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}