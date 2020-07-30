package de.codecentric.ddd.hexagonal.domain.shoppingcart.api;

import java.util.List;

public interface OrdersCheckoutPolicyService {
  void invoke( List<ShoppingCartItem> items );
}
