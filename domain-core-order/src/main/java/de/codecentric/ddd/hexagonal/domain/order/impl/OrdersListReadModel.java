package de.codecentric.ddd.hexagonal.domain.order.impl;

import de.codecentric.ddd.hexagonal.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersListRow;
import de.codecentric.ddd.hexagonal.domain.product.api.OrdersListRepository;

import java.util.List;

public class OrdersListReadModel {
  private final OrdersListRepository repository;

  public OrdersListReadModel( final OrdersListRepository repository ) {
    this.repository = repository;
  }

  public List<OrdersListRow> read() {
    return repository.findAll();
  }

  public void onOrderCreated( final Order order) {
    final OrdersListRow listRow =
      new OrdersListRow( order.getId(), order.getTotal(), order.getPositions(), order.getTimestamp() );
    repository.create(listRow);
  }
}
