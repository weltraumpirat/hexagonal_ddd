package de.codecentric.ddd.hexagonal.domain.shoppingcart.api;

import java.util.UUID;

public interface ShoppingCartsCheckoutPolicyService {
  UUID invoke( UUID cartId );
}
