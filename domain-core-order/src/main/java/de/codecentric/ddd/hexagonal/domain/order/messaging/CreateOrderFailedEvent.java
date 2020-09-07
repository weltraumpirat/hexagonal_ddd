package de.codecentric.ddd.hexagonal.domain.order.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;

import java.util.UUID;

public class CreateOrderFailedEvent extends Message<String> {
  public static final String CREATE_ORDER_FAILED = "CREATE_ORDER_FAILED";

  public CreateOrderFailedEvent( final UUID correlationId, final String message ) {
    super( correlationId, CREATE_ORDER_FAILED, message );
  }

  public CreateOrderFailedEvent( final String message ) {
    super( CREATE_ORDER_FAILED, message );
  }
}
