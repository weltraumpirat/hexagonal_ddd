package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCartsApi;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCartsCheckoutPolicyService;

import java.util.UUID;

public class ShoppingCartsCheckoutPolicyServiceInMemory implements ShoppingCartsCheckoutPolicyService {
  private final ShoppingCartsApi api;

  public ShoppingCartsCheckoutPolicyServiceInMemory(
    final ShoppingCartsApi api ) {
    this.api = api;
  }

  @Override public UUID invoke( final UUID cartId ) {
    api.deleteCartById( cartId );
    return api.createEmptyShoppingCart();
  }
}
