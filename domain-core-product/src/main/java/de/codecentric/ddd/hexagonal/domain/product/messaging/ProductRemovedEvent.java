package de.codecentric.ddd.hexagonal.domain.product.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;

import java.util.UUID;

public class ProductRemovedEvent extends Event<UUID> {

  public static final String PRODUCT_REMOVED = "PRODUCT_REMOVED";

  public ProductRemovedEvent( final UUID payload ) {
    super( PRODUCT_REMOVED, payload );
  }

  public ProductRemovedEvent( final UUID correlationId, final UUID payload ) {
    super( correlationId, PRODUCT_REMOVED, payload );
  }
}
