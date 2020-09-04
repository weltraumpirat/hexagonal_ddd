package de.codecentric.ddd.hexagonal.domain.order.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;

public class CreateOrderFailedEvent extends Message<String> {
  public static final String CREATE_ORDER_FAILED = "CREATE_ORDER_FAILED";

  public CreateOrderFailedEvent( final String s ) {
    super(CREATE_ORDER_FAILED, s);
  }
}
