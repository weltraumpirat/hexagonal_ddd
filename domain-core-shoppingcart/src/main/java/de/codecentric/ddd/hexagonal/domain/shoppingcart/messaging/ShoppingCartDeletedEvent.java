package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;

import java.util.UUID;

public class ShoppingCartDeletedEvent extends Event<UUID> {
  public static final String SHOPPING_CART_DELETED = "SHOPPING_CART_DELETED";

  public ShoppingCartDeletedEvent( final UUID payload ) {
    super( SHOPPING_CART_DELETED, payload );
  }

  public ShoppingCartDeletedEvent( final UUID correlationId, final UUID payload ) {
    super( correlationId, SHOPPING_CART_DELETED, payload );
  }
}
