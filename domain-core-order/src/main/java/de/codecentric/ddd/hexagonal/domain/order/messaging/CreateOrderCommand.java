package de.codecentric.ddd.hexagonal.domain.order.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Command;
import de.codecentric.ddd.hexagonal.domain.order.api.Order;

public class CreateOrderCommand extends Command<Order> {
  public static final String CREATE_ORDER = "CREATE_ORDER";

  public CreateOrderCommand( final Order order ) {
    super( CREATE_ORDER, order );
  }
}
