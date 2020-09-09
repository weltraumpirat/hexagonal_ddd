package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;

import java.util.UUID;

public class ShoppingCartUpdatedEvent extends Event<ShoppingCart> {
  public static final String SHOPPING_CART_UPDATED = "SHOPPING_CART_UPDATED";



  public ShoppingCartUpdatedEvent( final ShoppingCart payload ) {
    super( SHOPPING_CART_UPDATED, payload );
  }

  public ShoppingCartUpdatedEvent( final UUID correlationId, final ShoppingCart payload ) {
    super( correlationId, SHOPPING_CART_UPDATED, payload );
  }
}
