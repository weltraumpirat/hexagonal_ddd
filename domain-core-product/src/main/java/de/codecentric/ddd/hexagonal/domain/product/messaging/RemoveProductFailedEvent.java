package de.codecentric.ddd.hexagonal.domain.product.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;

import java.util.UUID;

public class RemoveProductFailedEvent extends Event<String>  {

  public static final String REMOVE_PRODUCT_FAILED = "REMOVE_PRODUCT_FAILED";

  public RemoveProductFailedEvent( final String message ) {
    super( REMOVE_PRODUCT_FAILED, message );
  }

  public RemoveProductFailedEvent( final UUID correlationId, final String payload ) {
    super( correlationId, REMOVE_PRODUCT_FAILED, payload );
  }
}
