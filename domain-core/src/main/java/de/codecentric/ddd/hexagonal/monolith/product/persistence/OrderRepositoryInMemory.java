package de.codecentric.ddd.hexagonal.monolith.product.persistence;

import de.codecentric.ddd.hexagonal.monolith.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrderRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderRepositoryInMemory implements OrderRepository {
  private final Map<UUID, Order> orders = new HashMap<>();

  @Override public void create( final Order order ) {
    this.orders.put(order.getId(), order);
  }

  @Override public List<Order> findAll() {
    return this.orders.values().stream().collect( Collectors.toUnmodifiableList());
  }
}
