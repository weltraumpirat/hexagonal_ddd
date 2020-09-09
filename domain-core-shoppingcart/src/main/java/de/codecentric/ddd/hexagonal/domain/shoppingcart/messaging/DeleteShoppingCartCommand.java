package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;

import java.util.UUID;

public class DeleteShoppingCartCommand extends Message<UUID> {

  public static final String DELETE_SHOPPING_CART = "DELETE_SHOPPING_CART";

  public DeleteShoppingCartCommand( final UUID payload ) {
    super( DELETE_SHOPPING_CART, payload );
  }

  public DeleteShoppingCartCommand( final UUID correlationId, final UUID payload ) {
    super( correlationId, DELETE_SHOPPING_CART, payload );
  }
}
