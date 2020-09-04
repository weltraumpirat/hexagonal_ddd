package de.codecentric.ddd.hexagonal.domain.order.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.common.messaging.TransactionResult;
import de.codecentric.ddd.hexagonal.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.domain.order.messaging.OrderCreatedEvent;
import lombok.extern.java.Log;

@Log
public class OrderCreatedHandler {
  private final OrdersListReadModel      ordersListReadModel;
  private final Order                    expected;
  public final  TransactionResult<Order> result;

  public OrderCreatedHandler( final OrdersListReadModel ordersListReadModel, final Order expected ) {
    this.ordersListReadModel = ordersListReadModel;
    this.expected = expected;
    this.result = new TransactionResult<>();
  }

  public void accept( final Message<?> msg ) {
    final Order created = ( (OrderCreatedEvent) msg ).getPayload();
    if( created.getId().equals( expected.getId() ) ) {
      ordersListReadModel.onOrderCreated( created );
      synchronized( result ) {
        result.complete( created );
        result.notify();
      }
    }
  }

  public final void waitForTransactionResult() {
    while( !result.complete ) {
      try {
        synchronized( result ) {
          result.wait( 1000 );
        }
      } catch( final InterruptedException e ) {
        log.warning( "Exception while waiting for transaction result: "+e.getMessage() );
      }
    }
  }


}
