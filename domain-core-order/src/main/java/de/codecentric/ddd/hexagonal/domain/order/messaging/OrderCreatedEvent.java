package de.codecentric.ddd.hexagonal.domain.order.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;
import de.codecentric.ddd.hexagonal.domain.order.api.Order;

public class OrderCreatedEvent extends Event<Order> {
  public static final String ORDER_CREATED = "ORDER_CREATED";

  public OrderCreatedEvent( final Order order ) {
    super(ORDER_CREATED, order);
  }
}
