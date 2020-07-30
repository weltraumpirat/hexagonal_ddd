package de.codecentric.ddd.hexagonal.domain.order.impl;

import de.codecentric.ddd.hexagonal.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.domain.order.api.OrderRepository;

import java.util.List;

public class OrdersApiImpl implements OrdersApi {

  private final OrderRepository repository;

  public OrdersApiImpl( final OrderRepository repository ) {
    this.repository = repository;
  }

  @Override public List<Order> getOrders() {
    return this.repository.findAll();
  }

  @Override public void createOrder( final Order order ) {
    this.repository.create( order );
  }
}
