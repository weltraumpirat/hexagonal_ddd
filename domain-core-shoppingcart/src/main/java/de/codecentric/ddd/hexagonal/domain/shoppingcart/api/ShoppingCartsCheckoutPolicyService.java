package de.codecentric.ddd.hexagonal.domain.shoppingcart.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ShoppingCartsCheckoutPolicyService {
  CompletableFuture<UUID> invoke( UUID correlationId, UUID cartId );
}
