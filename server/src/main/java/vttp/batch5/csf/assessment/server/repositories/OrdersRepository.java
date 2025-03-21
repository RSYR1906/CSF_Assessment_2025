package vttp.batch5.csf.assessment.server.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp.batch5.csf.assessment.server.model.MenuItem;
import vttp.batch5.csf.assessment.server.model.Order;

@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate mongoTemplate;

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  // Native MongoDB query here
  // db.menus.find({}).sort({name:1})
  public List<MenuItem> getMenus() {
    // Create query with ascending sort on name field
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.ASC, "name"));

    // Execute query against the menus collection
    List<MenuItem> menus = mongoTemplate.find(query, MenuItem.class, "menus");
    return menus;
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  // Native MongoDB query here
  // db.orders.insertOne({
  // order_id: "8 char id",
  // payment_id: "payment id from API",
  // username: "username",
  // total: totalAmount,
  // timestamp: new Date(),
  // items: [array of ordered items]
  // })
  public Order saveOrder(Order order) {
    try {
      System.out.println("Saving order to MongoDB: " + order);
      return mongoTemplate.insert(order, "orders");
    } catch (Exception e) {
      System.err.println("Error saving order to MongoDB: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}