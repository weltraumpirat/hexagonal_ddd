package de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ItemAndCartId;

import java.util.UUID;

public class AddItemToShoppingCartCommand extends Message<ItemAndCartId> {

  public static final String ADD_ITEM_TO_SHOPPING_CART = "ADD_ITEM_TO_SHOPPING_CART";

  public AddItemToShoppingCartCommand( final ItemAndCartId payload ) {
    super( ADD_ITEM_TO_SHOPPING_CART, payload );
  }

  public AddItemToShoppingCartCommand( final UUID correlationId, final ItemAndCartId payload ) {
    super( correlationId, ADD_ITEM_TO_SHOPPING_CART, payload );
  }
}
