package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;

import java.util.UUID;

public class CheckOutShoppingCartCommand extends Message<UUID> {

  public static final String CHECK_OUT_SHOPPING_CART = "CHECK_OUT_SHOPPING_CART";

  public CheckOutShoppingCartCommand( final UUID payload ) {
    super( CHECK_OUT_SHOPPING_CART, payload );
  }

  public CheckOutShoppingCartCommand( final UUID correlationId, final UUID payload ) {
    super( correlationId, CHECK_OUT_SHOPPING_CART, payload );
  }
}
