package de.codecentric.ddd.hexagonal.domain.order.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Transaction;
import de.codecentric.ddd.hexagonal.domain.common.messaging.TransactionFactory;
import de.codecentric.ddd.hexagonal.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersListRow;
import de.codecentric.ddd.hexagonal.domain.order.messaging.CreateOrderCommand;
import de.codecentric.ddd.hexagonal.domain.order.messaging.CreateOrderFailedEvent;
import de.codecentric.ddd.hexagonal.domain.order.messaging.OrderCreatedEvent;
import lombok.extern.java.Log;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Log
public class OrdersApiImpl implements OrdersApi {
  private final OrdersListReadModel ordersListReadModel;
  @SuppressWarnings( { "FieldCanBeLocal", "unused" } )
  private final OrdersFixture       ordersFixture;
  private final TransactionFactory  transactionFactory;

  public OrdersApiImpl(
    final OrdersFixture ordersFixture,
    final OrdersListReadModel ordersListReadModel, final TransactionFactory transactionFactory ) {
    this.ordersFixture = ordersFixture;
    this.transactionFactory = transactionFactory;
    this.ordersListReadModel = ordersListReadModel;
  }

  @Override public List<OrdersListRow> getOrders() {
    return ordersListReadModel.read();
  }

  @Override public CompletableFuture<Order> createOrder( final Order order ) {
    final UUID correlationId = UUID.randomUUID();
    final Transaction<Order, Order> transaction = transactionFactory.create(
      new CreateOrderCommand( correlationId, order ),
      OrderCreatedEvent.class,
      new CreateOrderFailedEvent( correlationId, "Timed out waiting for ORDER_CREATED." ) );
    return transaction.run();

  }
}


