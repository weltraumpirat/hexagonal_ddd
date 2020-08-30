package de.codecentric.ddd.hexagonal.domain.order.impl;

import de.codecentric.ddd.hexagonal.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.domain.order.api.OrderRepository;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersListRow;
import static de.codecentric.ddd.hexagonal.domain.product.api.OrdersListRepository.DATE_TIME_FORMATTER;

import java.time.LocalDateTime;
import java.util.List;

public class OrdersApiImpl implements OrdersApi {
  private final OrderRepository repository;
  private final OrdersListReadModel ordersListReadModel;

  public OrdersApiImpl( final OrderRepository repository,
                        final OrdersListReadModel ordersListReadModel ) {
    this.repository = repository;
    this.ordersListReadModel = ordersListReadModel;
  }

  @Override public List<OrdersListRow> getOrders() {
    return ordersListReadModel.read();
  }

  @Override public void createOrder( final Order order ) {
    final Order saved = new Order( order.getId(),
                                   order.getTotal(),
                                   order.getPositions(),
                                   order.getTimestamp() != null
                                   ? order.getTimestamp()
                                   : LocalDateTime.now().format( DATE_TIME_FORMATTER ) );
    this.repository.create( saved );
    ordersListReadModel.onOrderCreated( saved );
  }
}
