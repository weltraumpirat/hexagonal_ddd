package de.codecentric.ddd.hexagonal.domain.order.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersListRow;
import de.codecentric.ddd.hexagonal.domain.order.messaging.CreateOrderCommand;
import de.codecentric.ddd.hexagonal.domain.order.messaging.CreateOrderFailedEvent;
import de.codecentric.ddd.hexagonal.domain.order.messaging.OrderCreatedEvent;
import lombok.extern.java.Log;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Log
public class OrdersApiImpl implements OrdersApi {
  private final Messagebus          commandbus;
  private final OrdersListReadModel ordersListReadModel;
  private final Messagebus          eventbus;

  public OrdersApiImpl( final OrdersListReadModel ordersListReadModel,
                        final Messagebus eventbus,
                        final Messagebus commandbus,
                        final CreateOrderHandler createOrderHandler ) {
    this.ordersListReadModel = ordersListReadModel;
    this.eventbus = eventbus;
    this.commandbus = commandbus;
    commandbus.register( CreateOrderCommand.class, createOrderHandler::accept );
  }

  @Override public List<OrdersListRow> getOrders() {
    return ordersListReadModel.read();
  }

  @Override public CompletableFuture<Void> createOrder( final Order order ) {
    final OrderCreatedHandler handler = new OrderCreatedHandler( ordersListReadModel, order );
    final Runnable register = () -> eventbus.register( OrderCreatedEvent.class, handler::accept );
    final Runnable unregister = () -> eventbus.unregister( OrderCreatedEvent.class, handler::accept );
    final Runnable sendCreateOrderCommand = () -> commandbus.send( new CreateOrderCommand( order ) );
    final Function<Throwable, ? extends Void> timeoutHandler = ( final Throwable e ) -> {
      eventbus.send( new CreateOrderFailedEvent( e.getMessage() ) );
      return null;
    };
    return
      CompletableFuture
        .runAsync( register )
        .thenRunAsync( sendCreateOrderCommand )
        .runAfterBothAsync(
          CompletableFuture.runAsync( handler::waitForTransactionResult )
            .orTimeout( 3, TimeUnit.SECONDS )
            .exceptionally( timeoutHandler )
          , unregister );
  }
}


