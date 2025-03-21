package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.model.MenuItem;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

  @Autowired
  private RestaurantService restaurantService;

  // Task 2.2
  @GetMapping(path = "/menu")
  public ResponseEntity<String> getMenus() {
    List<MenuItem> menuItems = restaurantService.getMenus();

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (MenuItem menuItem : menuItems) {
      JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
          .add("id", menuItem.getId())
          .add("name", menuItem.getName())
          .add("price", menuItem.getPrice())
          .add("description", menuItem.getDescription());

      arrayBuilder.add(objectBuilder);
    }

    JsonArray menuArray = arrayBuilder.build();
    return ResponseEntity.ok(menuArray.toString());
  }

  // Task 4
  @PostMapping(path = "/food_order", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    try {
      // Parse the JSON payload
      JsonReader reader = Json.createReader(new StringReader(payload));
      JsonObject json = reader.readObject();

      // Extract required fields
      String username = json.getString("username");
      String password = json.getString("password");

      // Ensure totalPrice is present and valid
      double totalPrice;
      if (json.containsKey("totalPrice")) {
        totalPrice = json.getJsonNumber("totalPrice").doubleValue();
      } else {
        // If totalPrice is not in the request, return an error
        JsonObjectBuilder errorBuilder = Json.createObjectBuilder()
            .add("status", "error")
            .add("message", "Missing totalPrice in request");
        return ResponseEntity.badRequest().body(errorBuilder.build().toString());
      }

      // Process the order with authentication
      Map<String, String> result = restaurantService.processOrder(username, password, totalPrice);

      // Create response based on result
      JsonObjectBuilder responseBuilder = Json.createObjectBuilder();

      if ("error".equals(result.get("status"))) {
        // Authentication or payment failed
        responseBuilder
            .add("status", "error")
            .add("message", result.getOrDefault("message", "Order processing failed"));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(responseBuilder.build().toString());
      } else {
        // Order successful
        responseBuilder
            .add("status", "success")
            .add("orderId", result.get("orderId"))
            .add("paymentId", result.get("paymentId"))
            .add("date", result.get("date"))
            .add("total", result.get("total"));

        return ResponseEntity.ok(responseBuilder.build().toString());
      }
    } catch (Exception e) {
      e.printStackTrace(); // Log the error for debugging

      JsonObjectBuilder errorBuilder = Json.createObjectBuilder()
          .add("status", "error")
          .add("message", "Server error: " + e.getMessage());

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorBuilder.build().toString());
    }
  }
}