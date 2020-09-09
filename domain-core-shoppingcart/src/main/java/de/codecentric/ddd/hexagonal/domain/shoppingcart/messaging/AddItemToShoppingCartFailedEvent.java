package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;

import java.util.UUID;

public class AddItemToShoppingCartFailedEvent extends Event<String>  {

  public static final String ADD_ITEM_TO_SHOPPING_CART_FAILED = "ADD_ITEM_TO_SHOPPING_CART_FAILED";

  public AddItemToShoppingCartFailedEvent( final UUID correlationId, final String message ) {
    super( correlationId, ADD_ITEM_TO_SHOPPING_CART_FAILED, message );
  }

  public AddItemToShoppingCartFailedEvent( final String message ) {
    super( ADD_ITEM_TO_SHOPPING_CART_FAILED, message );
  }

}
