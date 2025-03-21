package vttp.batch5.csf.assessment.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import vttp.batch5.csf.assessment.model.Menu;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;

public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepository;

  // TODO: Task 2.2
  // You may change the method's signature
  public List<Menu> getMenus() {
    List<Menu> menuList = ordersRepository.getMenus();
    return menuList;
  }

  // TODO: Task 4

}
