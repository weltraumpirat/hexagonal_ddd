package de.codecentric.ddd.hexagonal.domain.order.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Command;
import de.codecentric.ddd.hexagonal.domain.order.api.Order;

import java.util.UUID;

public class CreateOrderCommand extends Command<Order> {
  public static final String CREATE_ORDER = "CREATE_ORDER";

  public CreateOrderCommand( final UUID correlationId, final Order order ) {
    super( correlationId, CREATE_ORDER, order );
  }

  public CreateOrderCommand( final Order order ) {
    super( CREATE_ORDER, order );
  }
}
