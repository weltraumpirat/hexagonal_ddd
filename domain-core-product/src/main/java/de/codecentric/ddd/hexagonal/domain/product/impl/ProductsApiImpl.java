package de.codecentric.ddd.hexagonal.domain.product.impl;


import de.codecentric.ddd.hexagonal.domain.common.messaging.Transaction;
import de.codecentric.ddd.hexagonal.domain.common.messaging.TransactionFactory;
import de.codecentric.ddd.hexagonal.domain.product.api.*;
import de.codecentric.ddd.hexagonal.domain.product.messaging.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ProductsApiImpl implements ProductsApi {
  @SuppressWarnings( { "FieldCanBeLocal", "unused" } )
  private final ProductsFixture              fixture;
  private final ProductValidationReadModel   validationReadModel;
  private final ProductListReadModel         productListReadModel;
  private final ProductShoppingListReadModel productShoppingListReadModel;
  private final TransactionFactory           transactionFactory;

  public ProductsApiImpl( final ProductsFixture fixture,
                          final ProductValidationReadModel validationReadModel,
                          final ProductListReadModel productListReadModel,
                          final ProductShoppingListReadModel productShoppingListReadModel,
                          final TransactionFactory transactionFactory) {
    this.fixture = fixture;
    this.validationReadModel = validationReadModel;
    this.productListReadModel = productListReadModel;
    this.productShoppingListReadModel = productShoppingListReadModel;
    this.transactionFactory = transactionFactory;
  }

  @Override public CompletableFuture<Product> addProduct( final Product product ) {
    final UUID correlationId = UUID.randomUUID();
    final Transaction<Product, Product> transaction = transactionFactory.create(
      new AddProductCommand( correlationId, product ),
      ProductCreatedEvent.class,
      new AddProductFailedEvent( "Timed out while waiting for PRODUCT_CREATED" ) );
    return transaction.run();
  }

  @Override public CompletableFuture<UUID> removeProduct( final UUID id ) {
    final UUID correlationId = UUID.randomUUID();
    final Transaction<UUID, UUID> transaction = transactionFactory.create(
      new RemoveProductCommand( correlationId, id ),
      ProductRemovedEvent.class,
      new RemoveProductFailedEvent( "Timed out while waiting for PRODUCT_REMOVED." ) );
    return transaction.run();
  }

  @Override public List<ProductListRow> getProductList() {
    return productListReadModel.read();
  }

  @Override public List<ProductShoppingListRow> getProductShoppingList() {
    return productShoppingListReadModel.read();
  }

  @Override public ProductValidationEntry validateProduct( final String label ) {
    return validationReadModel.read( label );
  }
}
