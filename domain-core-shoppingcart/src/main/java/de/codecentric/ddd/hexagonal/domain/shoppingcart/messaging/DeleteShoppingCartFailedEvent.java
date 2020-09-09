package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;

import java.util.UUID;

public class DeleteShoppingCartFailedEvent extends Event<String>  {
  public DeleteShoppingCartFailedEvent( final String message ) {
    super("DELETE_SHOPPING_CART_FAILED", message);
  }
  public DeleteShoppingCartFailedEvent( final UUID correlationId, final String message ) {
    super(correlationId, "DELETE_SHOPPING_CART_FAILED", message);
  }
}
