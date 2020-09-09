package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;

import java.util.UUID;

public class ShoppingCartCheckedOutEvent extends Event<UUID> {
  public static final String SHOPPING_CART_CHECKED_OUT = "SHOPPING_CART_CHECKED_OUT";

  public ShoppingCartCheckedOutEvent( final UUID payload ) {
    super( SHOPPING_CART_CHECKED_OUT, payload );
  }

  public ShoppingCartCheckedOutEvent( final UUID correlationId, final UUID payload ) {
    super( correlationId, SHOPPING_CART_CHECKED_OUT, payload );
  }
}
