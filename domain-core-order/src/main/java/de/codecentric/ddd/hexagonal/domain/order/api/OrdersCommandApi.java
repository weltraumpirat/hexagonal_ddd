package de.codecentric.ddd.hexagonal.domain.order.api;

public interface OrdersCommandApi {
  void createOrder( Order order );
}
