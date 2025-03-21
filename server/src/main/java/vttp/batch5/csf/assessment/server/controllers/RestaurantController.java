package vttp.batch5.csf.assessment.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import jakarta.json.JsonObjectBuilder;
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
  @PostMapping(path = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    return ResponseEntity.ok("{}");
  }
}