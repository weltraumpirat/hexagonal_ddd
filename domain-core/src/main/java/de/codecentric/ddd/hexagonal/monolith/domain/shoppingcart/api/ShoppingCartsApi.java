package de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartsApi {

  UUID createEmptyShoppingCart();

  void addItemToShoppingCart(final UUID cartId,  final ShoppingCartItem shoppingCartItem );

  List<ShoppingCart> getShoppingCarts();

  List<ShoppingCartItem> getShoppingCartItems(final UUID cartId );

  void removeItemFromShoppingCart( final UUID cartId, final UUID itemId );

  UUID checkOut( final UUID cartId );

  ShoppingCart getShoppingCartById( UUID cartId );

  void deleteCartById( UUID cartId );
}
