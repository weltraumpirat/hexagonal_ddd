package de.codecentric.ddd.hexagonal.domain.order.api;

import java.util.concurrent.CompletableFuture;

public interface OrdersCommandApi {
  CompletableFuture<Order> createOrder( Order order );
}
