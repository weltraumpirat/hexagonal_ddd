package de.codecentric.ddd.hexagonal.domain.shoppingcart.api;

import java.util.UUID;

public interface ShoppingCartCommandsApi {
  UUID createEmptyShoppingCart();

  void deleteCartById( UUID cartId );

  void addItemToShoppingCart( final UUID cartId, final ShoppingCartItem shoppingCartItem );

  void removeItemFromShoppingCart( final UUID cartId, final UUID itemId );

  UUID checkOut( final UUID cartId );

}
