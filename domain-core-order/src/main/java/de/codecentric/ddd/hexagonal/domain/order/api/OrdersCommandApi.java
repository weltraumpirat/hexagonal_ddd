package de.codecentric.ddd.hexagonal.domain.order.api;

import java.util.concurrent.CompletableFuture;

public interface OrdersCommandApi {
  CompletableFuture<Void> createOrder( Order order );
}
