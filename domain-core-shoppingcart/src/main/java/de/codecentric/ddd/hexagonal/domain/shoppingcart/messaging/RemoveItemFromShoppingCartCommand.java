package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ItemIdAndCartId;

import java.util.UUID;

public class RemoveItemFromShoppingCartCommand extends Message<ItemIdAndCartId> {

  public static final String REMOVE_ITEM_FROM_SHOPPING_CART = "REMOVE_ITEM_FROM_SHOPPING_CART";

  public RemoveItemFromShoppingCartCommand( final ItemIdAndCartId payload ) {
    super( REMOVE_ITEM_FROM_SHOPPING_CART, payload );
  }

  public RemoveItemFromShoppingCartCommand( final UUID correlationId, final ItemIdAndCartId payload ) {
    super( correlationId, REMOVE_ITEM_FROM_SHOPPING_CART, payload );
  }
}
