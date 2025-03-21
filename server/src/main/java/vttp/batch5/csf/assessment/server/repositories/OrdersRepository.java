package vttp.batch5.csf.assessment.server.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp.batch5.csf.assessment.model.Menu;

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
  public List<Menu> getMenus() {
    // Create query with ascending sort on name field
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.ASC, "name"));

    // Execute query against the menus collection
    List<Menu> menus = mongoTemplate.find(query, Menu.class, "menus");
    return menus;
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  // Native MongoDB query here

}
