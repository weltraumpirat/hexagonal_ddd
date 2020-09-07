package de.codecentric.ddd.hexagonal.domain.product.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;
import de.codecentric.ddd.hexagonal.domain.product.api.Product;

import java.util.UUID;

public class ProductCreatedEvent extends Event<Product> {

  public static final String PRODUCT_CREATED = "PRODUCT_CREATED";

  public ProductCreatedEvent( final Product payload ) {
    super( PRODUCT_CREATED, payload );
  }

  public ProductCreatedEvent( final UUID correlationId, final Product payload ) {
    super( correlationId, PRODUCT_CREATED, payload );
  }
}
