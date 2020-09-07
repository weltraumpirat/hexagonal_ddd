package de.codecentric.ddd.hexagonal.domain.product.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;

public class AddProductFailedEvent extends Event<String> {
  public static final String ADD_PRODUCT_FAILED = "ADD_PRODUCT_FAILED";

  public AddProductFailedEvent( final String message ) {
    super( ADD_PRODUCT_FAILED, message );
  }
}
