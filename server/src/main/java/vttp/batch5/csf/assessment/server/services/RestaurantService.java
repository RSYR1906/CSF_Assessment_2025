// In RestaurantService.java
package vttp.batch5.csf.assessment.server.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batch5.csf.assessment.model.MenuItem;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;

  // Task 2.2
  public List<MenuItem> getMenus() {
    List<MenuItem> menuList = ordersRepository.getMenus();
    return menuList;
  }

  // Task 4
  public Map<String, String> processOrder(String username, String password, double total) {
    // Authenticate user
    boolean isAuthenticated = restaurantRepository.authenticateUser(username, password);

    if (!isAuthenticated) {
      return Map.of("status", "error", "message", "Invalid username or password");
    }

    // Process the order if authentication is successful
    String orderDetails = restaurantRepository.saveOrder(username, total);
    String[] parts = orderDetails.split(":");

    return Map.of(
        "status", "success",
        "orderId", parts[0],
        "paymentId", parts[1],
        "date", LocalDate.now().toString(),
        "total", String.valueOf(total));
  }
}