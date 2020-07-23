package de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartsApi {
  List<ShoppingCart> getShoppingCarts();

  UUID createEmptyShoppingCart();

  void addItemToShoppingCart(final UUID cartId,  final ShoppingCartItem shoppingCartItem );

  List<ShoppingCartItem> getShoppingCartItems(final UUID cartId );

  void removeItemFromShoppingCart( final UUID cartId, final UUID itemId );

  void checkOut( final UUID cartId );
}
