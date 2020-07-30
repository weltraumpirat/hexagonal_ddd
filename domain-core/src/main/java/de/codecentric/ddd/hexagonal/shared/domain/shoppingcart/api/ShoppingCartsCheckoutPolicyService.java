package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api;

import java.util.UUID;

public interface ShoppingCartsCheckoutPolicyService {
  UUID invoke( UUID cartId );
}
