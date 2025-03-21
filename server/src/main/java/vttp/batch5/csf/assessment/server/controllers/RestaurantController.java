package vttp.batch5.csf.assessment.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class RestaurantController {

  // TODO: Task 2.2
  // You may change the method's signature
  public ResponseEntity<String> getMenus() {
    return ResponseEntity.ok("{}");
  }

  // TODO: Task 4
  // Do not change the method's signature
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    return ResponseEntity.ok("{}");
  }
}
