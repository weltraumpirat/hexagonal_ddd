package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCartsApi;

import java.util.UUID;

public class ShoppingCartsCheckoutPolicyService {
  private final ShoppingCartsApi api;

  public ShoppingCartsCheckoutPolicyService(
    final ShoppingCartsApi api ) {
    this.api = api;
  }

  public UUID invoke(final UUID cartId) {
    api.deleteCartById( cartId );
    return api.createEmptyShoppingCart();
  }
}
