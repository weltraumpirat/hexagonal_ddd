package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Transaction;
import de.codecentric.ddd.hexagonal.domain.common.messaging.TransactionFactory;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.*;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.messaging.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ShoppingCartsApiImpl implements ShoppingCartsApi {
  @SuppressWarnings( { "FieldCanBeLocal", "unused" } )
  private final ShoppingCartFixture    shoppingCartFixture;
  private final ShoppingCartListReadModel
                                       shoppingCartListReadModel;
  private final ShoppingCartItemsReadModel
                                       shoppingCartItemsReadModel;
  private final TransactionFactory     transactionFactory;

  public ShoppingCartsApiImpl(
    final ShoppingCartFixture shoppingCartFixture,
    final ShoppingCartListReadModel shoppingCartListReadModel,
    final ShoppingCartItemsReadModel shoppingCartItemsReadModel,
    final TransactionFactory transactionFactory ) {
    this.shoppingCartFixture = shoppingCartFixture;
    this.shoppingCartListReadModel = shoppingCartListReadModel;
    this.shoppingCartItemsReadModel = shoppingCartItemsReadModel;
    this.transactionFactory = transactionFactory;
  }

  @Override public CompletableFuture<UUID> createEmptyShoppingCart() {
    final UUID correlationId = UUID.randomUUID();
    final Transaction<Void, ShoppingCart> transaction = transactionFactory.create(
      new CreateShoppingCartCommand( correlationId ),
      ShoppingCartCreatedEvent.class,
      new CreateShoppingCartFailedEvent( correlationId, "Timed out while waiting for SHOPPING_CART_CREATED." ) );
    return transaction.run().thenApply( ShoppingCart::getId );
  }

  @Override public CompletableFuture<Void> deleteCartById( final UUID cartId ) {
    final UUID correlationId = UUID.randomUUID();
    final Transaction<UUID, Void> transaction = transactionFactory.create(
      new DeleteShoppingCartCommand( correlationId, cartId ),
      ShoppingCartDeletedEvent.class,
      new DeleteShoppingCartFailedEvent( correlationId, "Timed out waiting for SHOPPING_CART_DELETED." ) );
    return transaction.run();
  }

  @Override public CompletableFuture<Void> addItemToShoppingCart( final UUID cartId, final ShoppingCartItem item ) {
    final UUID correlationId = UUID.randomUUID();
    final Transaction<ItemAndCartId, Void> transaction = transactionFactory.create(
      new AddItemToShoppingCartCommand( correlationId, new ItemAndCartId( item, cartId ) ),
      ShoppingCartUpdatedEvent.class,
      new AddItemToShoppingCartFailedEvent( correlationId, "Timed out waiting for SHOPPING_CART_UPDATED." ) );
    return transaction.run();
  }

  @Override public CompletableFuture<Void> removeItemFromShoppingCart( final UUID cartId, final UUID itemId ) {
    final UUID correlationId = UUID.randomUUID();
    final Transaction<ItemIdAndCartId, Void> transaction = transactionFactory.create(
      new RemoveItemFromShoppingCartCommand( correlationId, new ItemIdAndCartId( itemId, cartId ) ),
      ShoppingCartUpdatedEvent.class,
      new RemoveItemFromShoppingCartFailedEvent( correlationId,
                                                 "Timed out while waiting for SHOPPING_CART_UPDATED." ) );
    return transaction.run();
  }

  @Override public CompletableFuture<UUID> checkOut( final UUID cartId ) {
    final UUID correlationId = UUID.randomUUID();
    final Transaction<UUID, UUID> transaction = transactionFactory.create(
      new CheckOutShoppingCartCommand( correlationId, cartId ),
      ShoppingCartCheckedOutEvent.class,
      new CheckOutShoppingCartFailedEvent( correlationId, "Timed out while waiting for SHOPPING_CART_CHECKED_OUT." ) );
    return transaction.run();
  }

  @Override public List<ShoppingCartListRow> getShoppingCarts() {
    return shoppingCartListReadModel.read();
  }

  @Override public ShoppingCartItemsInfo getShoppingCartItems( final UUID cartId ) {
    return shoppingCartItemsReadModel.read( cartId );
  }
}

