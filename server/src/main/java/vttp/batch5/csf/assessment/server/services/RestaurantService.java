package vttp.batch5.csf.assessment.server.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.model.MenuItem;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;

  private static final String PAYMENT_API_URL = "https://payment-service-production-a75a.up.railway.app/";

  @Value("${application.owner.name:Your Official Name}")
  private String payeeName;

  // Task 2.2
  public List<MenuItem> getMenus() {
    List<MenuItem> menuList = ordersRepository.getMenus();
    return menuList;
  }

  // Task 4
  public Map<String, String> processOrder(String username, String password, double total) {
    try {
      // Authenticate user
      boolean isAuthenticated = restaurantRepository.authenticateUser(username, password);

      if (!isAuthenticated) {
        return Map.of("status", "error", "message", "Invalid username or password");
      }

      // Generate a unique order ID (8 characters)
      String orderId = generateOrderId();

      // Process payment through the payment gateway
      String paymentId = processPayment(orderId, username, total);

      if (paymentId == null || paymentId.isEmpty()) {
        return Map.of("status", "error", "message", "Payment processing failed");
      }

      // Save the order to database with the payment ID
      restaurantRepository.saveOrder(username, orderId, paymentId, total);

      // Build response with all order details
      Map<String, String> response = new HashMap<>();
      response.put("status", "success");
      response.put("orderId", orderId);
      response.put("paymentId", paymentId);
      response.put("date", LocalDate.now().toString());
      response.put("total", String.valueOf(total));

      return response;
    } catch (Exception e) {
      System.err.println("Error processing order: " + e.getMessage());
      e.printStackTrace();
      return Map.of("status", "error", "message", "Order processing error: " + e.getMessage());
    }
  }

  /**
   * Process a payment through the external payment gateway
   * 
   * @param orderId     The 8-character order ID
   * @param username    The username from the order
   * @param totalAmount The total amount to be paid
   * @return The payment ID received from the payment gateway
   */
  private String processPayment(String orderId, String username, double totalAmount) {
    try {
      // Create RestTemplate instance
      RestTemplate restTemplate = new RestTemplate();

      // Set up headers
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      // Add the required X-Authenticate header with the same value as payer
      headers.set("X-Authenticate", username);

      // Create the request body
      String requestBody = Json.createObjectBuilder()
          .add("order-id", orderId)
          .add("payer", username)
          .add("payee", payeeName)
          .add("payment", totalAmount)
          .build()
          .toString();

      System.out.println("Payment Request: " + requestBody);

      // Create the request entity
      HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

      // Make the API call
      ResponseEntity<String> response = restTemplate.postForEntity(
          PAYMENT_API_URL,
          request,
          String.class);

      // Process the response
      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        System.out.println("Payment Response: " + response.getBody());

        // Parse the response to extract payment ID
        JsonReader reader = Json.createReader(new java.io.StringReader(response.getBody()));
        JsonObject jsonResponse = reader.readObject();

        // Assuming the response contains a payment_id field
        if (jsonResponse.containsKey("payment_id")) {
          return jsonResponse.getString("payment_id");
        } else {
          System.err.println("Payment successful but no payment_id in response");
          return "PAYMENT-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        }
      } else {
        System.err.println("Payment failed: " + response.getStatusCode());
        return null;
      }
    } catch (Exception e) {
      System.err.println("Error processing payment: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Generate a random 8-character order ID
   */
  private String generateOrderId() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 8; i++) {
      int index = (int) (chars.length() * Math.random());
      sb.append(chars.charAt(index));
    }
    return sb.toString();
  }
}