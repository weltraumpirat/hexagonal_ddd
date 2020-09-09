package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;

import java.util.UUID;

public class CreateShoppingCartFailedEvent extends Event<String> {

  public static final String CREATE_SHOPPING_CART_FAILED = "CREATE_SHOPPING_CART_FAILED";

  public CreateShoppingCartFailedEvent( final UUID correlationId, final String message ) {
    super( correlationId, CREATE_SHOPPING_CART_FAILED, message );
  }

  public CreateShoppingCartFailedEvent( final String message ) {
    super( CREATE_SHOPPING_CART_FAILED, message );
  }
}

