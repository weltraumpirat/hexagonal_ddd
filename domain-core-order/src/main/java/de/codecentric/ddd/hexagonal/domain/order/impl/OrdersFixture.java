package de.codecentric.ddd.hexagonal.domain.order.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.domain.order.api.OrderRepository;
import de.codecentric.ddd.hexagonal.domain.order.messaging.CreateOrderCommand;
import de.codecentric.ddd.hexagonal.domain.order.messaging.OrderCreatedEvent;
import static de.codecentric.ddd.hexagonal.domain.product.api.OrdersListRepository.DATE_TIME_FORMATTER;

import java.time.LocalDateTime;


public class OrdersFixture {
  private final OrderRepository repository;
  private final Messagebus      eventbus;

  public OrdersFixture( final OrderRepository repository,
                        final Messagebus eventbus,
                        final Messagebus commandbus ) {
    this.repository = repository;
    this.eventbus = eventbus;
    commandbus.register( CreateOrderCommand.class, this::handleCreateOrder );
  }

  public void handleCreateOrder( final Message<?> command ) {
    final Order order = ( (CreateOrderCommand) command ).getPayload();
    final Order saved = new Order( order.getId(),
                                   order.getTotal(),
                                   order.getPositions(),
                                   order.getTimestamp() != null
                                   ? order.getTimestamp()
                                   : LocalDateTime.now().format( DATE_TIME_FORMATTER ) );
    repository.create( saved );
    command.getCorrelationId()
      .ifPresentOrElse(
        correlationId -> eventbus.send( new OrderCreatedEvent( correlationId, saved ) ),
        () -> eventbus.send( new OrderCreatedEvent( saved ) ) );
  }
}
