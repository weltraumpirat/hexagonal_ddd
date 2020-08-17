package de.codecentric.ddd.hexagonal.domain.shoppingcart.api;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartQueriesApi {
  List<ShoppingCart> getShoppingCarts();
  List<ShoppingCartItem> getShoppingCartItems(final UUID cartId );
  ShoppingCart getShoppingCartById( UUID cartId );
}
