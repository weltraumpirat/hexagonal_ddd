package de.codecentric.ddd.hexagonal.domain.order.api;

import java.util.List;

public interface OrderRepository {
  void create( Order order );

  List<Order> findAll();
}