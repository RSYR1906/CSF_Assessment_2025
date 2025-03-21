package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.model.MenuItem;
import vttp.batch5.csf.assessment.server.model.OrderItem;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
  private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

  @Autowired
  private RestaurantService restaurantService;

  // Task 2.2
  @GetMapping(path = "/menu")
  public ResponseEntity<String> getMenus() {
    List<MenuItem> menuItems = restaurantService.getMenus();

    var arrayBuilder = Json.createArrayBuilder();
    for (MenuItem menuItem : menuItems) {
      arrayBuilder.add(Json.createObjectBuilder()
          .add("id", menuItem.getId())
          .add("name", menuItem.getName())
          .add("price", menuItem.getPrice())
          .add("description", menuItem.getDescription()));
    }

    return ResponseEntity.ok(arrayBuilder.build().toString());
  }

  // Task 4
  @PostMapping(path = "/food_order", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    try (JsonReader reader = Json.createReader(new StringReader(payload))) {
      JsonObject json = reader.readObject();

      String username = json.getString("username");
      String password = json.getString("password");

      if (!json.containsKey("totalPrice")) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Missing totalPrice in request");
      }
      double totalPrice = json.getJsonNumber("totalPrice").doubleValue();

      List<OrderItem> orderItems = extractOrderItems(json);

      Map<String, String> result = restaurantService.processOrder(username, password, totalPrice, orderItems);

      if ("error".equals(result.get("status"))) {
        return createErrorResponse(
            HttpStatus.UNAUTHORIZED,
            result.getOrDefault("message", "Order processing failed"));
      } else {
        var responseBuilder = Json.createObjectBuilder()
            .add("status", "success")
            .add("orderId", result.get("orderId"))
            .add("paymentId", result.get("paymentId"))
            .add("date", result.get("date"))
            .add("total", result.get("total"));

        return ResponseEntity.ok(responseBuilder.build().toString());
      }
    } catch (Exception e) {
      logger.error("Error processing food order", e);
      return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
    }
  }

  private List<OrderItem> extractOrderItems(JsonObject json) {
    List<OrderItem> orderItems = new ArrayList<>();

    if (json.containsKey("items") && json.get("items").getValueType() == jakarta.json.JsonValue.ValueType.ARRAY) {
      JsonArray itemsArray = json.getJsonArray("items");

      for (int i = 0; i < itemsArray.size(); i++) {
        JsonObject itemJson = itemsArray.getJsonObject(i);
        String menuItemId = itemJson.getString("menuItemId");
        int quantity = itemJson.getInt("quantity");
        double price = itemJson.containsKey("price")
            ? itemJson.getJsonNumber("price").doubleValue()
            : 0.0;

        orderItems.add(new OrderItem(menuItemId, quantity, price));
      }
    }

    return orderItems;
  }

  private ResponseEntity<String> createErrorResponse(HttpStatus status, String message) {
    var errorBuilder = Json.createObjectBuilder()
        .add("status", "error")
        .add("message", message);

    return ResponseEntity.status(status).body(errorBuilder.build().toString());
  }
}