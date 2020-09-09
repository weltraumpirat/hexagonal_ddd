package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;

import java.util.UUID;

public class CreateShoppingCartCommand extends Message<Void> {

  public static final String CREATE_SHOPPING_CART = "CREATE_SHOPPING_CART";

  public CreateShoppingCartCommand(  ) {
    super( CREATE_SHOPPING_CART, null );
  }

  public CreateShoppingCartCommand( final UUID correlationId ) {
    super( correlationId, CREATE_SHOPPING_CART, null );
  }
}
