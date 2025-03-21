package vttp.batch5.csf.assessment.server.repositories;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public boolean authenticateUser(String username, String password) {
    try {
      String sql = "SELECT COUNT(*) FROM customers WHERE username = ? AND password = SHA2(?, 224)";

      // System.out.println("Auth SQL: " + sql);
      // System.out.println(
      // "Auth params: username=" + username + ", password=" + (password != null ?
      // "[provided]" : "[null]"));

      Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
      // System.out.println("Auth result count: " + count);

      return count != null && count > 0;
    } catch (Exception e) {
      // System.err.println("Authentication error for user " + username + ": " +
      // e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  public void saveOrder(String username, String orderId, String paymentId, double total) {
    try {
      LocalDate orderDate = LocalDate.now();

      String sql = "INSERT INTO place_orders (order_id, payment_id, order_date, total, username) VALUES (?, ?, ?, ?, ?)";
      jdbcTemplate.update(sql, orderId, paymentId, orderDate, total, username);

      // System.out.println("Order saved successfully: ID=" + orderId + ", Payment=" +
      // paymentId);
    } catch (Exception e) {
      // System.err.println("Error saving order for user " + username + ": " +
      // e.getMessage());
      e.printStackTrace();
      throw new RuntimeException("Failed to save order: " + e.getMessage());
    }
  }
}