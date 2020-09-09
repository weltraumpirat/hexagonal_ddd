package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Event;

import java.util.UUID;

public class RemoveItemFromShoppingCartFailedEvent extends Event<String> {

  public static final String REMOVE_ITEM_FROM_SHOPPING_CART_FAILED = "REMOVE_ITEM_FROM_SHOPPING_CART_FAILED";

  public RemoveItemFromShoppingCartFailedEvent( final UUID correlationId, final String message ) {
    super( correlationId, REMOVE_ITEM_FROM_SHOPPING_CART_FAILED, message );
  }

  public RemoveItemFromShoppingCartFailedEvent(final String message) {
    super(REMOVE_ITEM_FROM_SHOPPING_CART_FAILED, message);
  }
}
