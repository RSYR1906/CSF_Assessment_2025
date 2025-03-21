// In RestaurantRepository.java
package vttp.batch5.csf.assessment.server.repositories;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
// Use the following class for MySQL database
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Authenticate user against customers table
    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT COUNT(*) FROM customers WHERE username = ? AND password = SHA2(?, 224)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
        return count != null && count > 0;
    }

    // Save order to the place_orders table
    public String saveOrder(String username, double total) {
        // Generate unique IDs
        String orderId = generateId(8);
        String paymentId = UUID.randomUUID().toString();
        LocalDate orderDate = LocalDate.now();

        String sql = "INSERT INTO place_orders (order_id, payment_id, order_date, total, username) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderId, paymentId, orderDate, total, username);

        return orderId + ":" + paymentId;
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