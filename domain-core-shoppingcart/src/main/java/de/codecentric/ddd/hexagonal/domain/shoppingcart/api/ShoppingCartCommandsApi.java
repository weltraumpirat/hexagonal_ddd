package de.codecentric.ddd.hexagonal.domain.shoppingcart.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ShoppingCartCommandsApi {
  CompletableFuture<UUID> createEmptyShoppingCart();

  CompletableFuture<Void> deleteCartById( UUID cartId );

  CompletableFuture<Void> addItemToShoppingCart( final UUID cartId, final ShoppingCartItem shoppingCartItem );

  CompletableFuture<Void> removeItemFromShoppingCart( final UUID cartId, final UUID itemId );

  CompletableFuture<UUID> checkOut( final UUID cartId );

}
