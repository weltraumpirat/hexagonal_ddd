package de.codecentric.ddd.hexagonal.domain.order.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersListRow;
import de.codecentric.ddd.hexagonal.domain.order.messaging.OrderCreatedEvent;
import de.codecentric.ddd.hexagonal.domain.product.api.OrdersListRepository;

import java.util.List;

public class OrdersListReadModel {
  private final OrdersListRepository repository;

  public OrdersListReadModel( final OrdersListRepository repository, final Messagebus eventbus ) {
    this.repository = repository;
    eventbus.register( OrderCreatedEvent.class, this::onOrderCreated);
  }

  public List<OrdersListRow> read() {
    return repository.findAll();
  }

  public void onOrderCreated( final Message<?> msg) {
    final Order order= ((OrderCreatedEvent) msg ).getPayload();
    final OrdersListRow listRow =
      new OrdersListRow( order.getId(), order.getTotal(), order.getPositions(), order.getTimestamp() );
    repository.create(listRow);
  }
}
