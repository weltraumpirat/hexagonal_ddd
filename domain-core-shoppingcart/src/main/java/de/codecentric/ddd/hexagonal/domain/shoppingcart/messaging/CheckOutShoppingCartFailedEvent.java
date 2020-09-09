package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;

import java.util.UUID;

public class CheckOutShoppingCartFailedEvent extends Event<String> {

  public static final String CHECK_OUT_SHOPPING_CART_FAILED = "CHECK_OUT_SHOPPING_CART_FAILED";

  public CheckOutShoppingCartFailedEvent( final UUID correlationId, final String message ) {
    super( correlationId, CHECK_OUT_SHOPPING_CART_FAILED, message );
  }

  public CheckOutShoppingCartFailedEvent( final String message ) {
    super( CHECK_OUT_SHOPPING_CART_FAILED, message );
  }
}
