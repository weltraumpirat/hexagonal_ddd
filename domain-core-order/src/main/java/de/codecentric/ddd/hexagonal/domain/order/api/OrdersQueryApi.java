package de.codecentric.ddd.hexagonal.domain.order.api;

import java.util.List;

public interface OrdersQueryApi {
  List<OrdersListRow> getOrders();
}
