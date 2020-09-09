package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Transaction;
import de.codecentric.ddd.hexagonal.domain.common.messaging.TransactionFactory;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartsCheckoutPolicyService;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging.*;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ShoppingCartsCheckoutPolicyServiceInMemory implements ShoppingCartsCheckoutPolicyService {
  private final TransactionFactory transactionFactory;

  public ShoppingCartsCheckoutPolicyServiceInMemory( final TransactionFactory transactionFactory ) {
    this.transactionFactory = transactionFactory;
  }

  @Override public CompletableFuture<UUID> invoke( final UUID correlationId, final UUID cartId ) {

    final Transaction<UUID, UUID> deleteTransaction =
      transactionFactory.create( new DeleteShoppingCartCommand( correlationId, cartId ),
                                 ShoppingCartDeletedEvent.class,
                                 new DeleteShoppingCartFailedEvent( correlationId,
                                                                    "Timed out while waiting for SHOPPING_CART_DELETED." ) );
    final Transaction<Void, ShoppingCart> createTransaction = transactionFactory.create(
      new CreateShoppingCartCommand( correlationId ),
      ShoppingCartCreatedEvent.class,
      new CreateShoppingCartFailedEvent( correlationId, "Timed out while waiting for SHOPPING_CART_CREATED." ) );
    return CompletableFuture.allOf( deleteTransaction.run(), createTransaction.run() )
             .thenApply( ignore -> createTransaction.getResult() )
             .thenApply( cart -> cart.orElseThrow( NoSuchElementException::new ).getId() );
  }
}
