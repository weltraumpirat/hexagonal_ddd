package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;

import java.util.UUID;

public class ShoppingCartCreatedEvent extends Event<ShoppingCart> {
  public static final String SHOPPING_CART_CREATED = "SHOPPING_CART_CREATED";

  public ShoppingCartCreatedEvent( final ShoppingCart payload ) {
    super( SHOPPING_CART_CREATED, payload );
  }

  public ShoppingCartCreatedEvent( final UUID correlationId, final ShoppingCart payload ) {
    super( correlationId, SHOPPING_CART_CREATED, payload );
  }
}
