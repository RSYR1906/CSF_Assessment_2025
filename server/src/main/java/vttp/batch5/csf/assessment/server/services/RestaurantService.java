package vttp.batch5.csf.assessment.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batch5.csf.assessment.model.MenuItem;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepository;

  // TODO: Task 2.2
  // You may change the method's signature
  public List<MenuItem> getMenus() {
    List<MenuItem> menuList = ordersRepository.getMenus();
    return menuList;
  }

  // TODO: Task 4

}
