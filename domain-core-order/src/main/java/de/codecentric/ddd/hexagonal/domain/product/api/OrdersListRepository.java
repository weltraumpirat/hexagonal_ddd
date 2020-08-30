package de.codecentric.ddd.hexagonal.domain.product.api;

import de.codecentric.ddd.hexagonal.domain.order.api.OrdersListRow;

import java.time.format.DateTimeFormatter;
import java.util.List;

public interface OrdersListRepository {
  DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  List<OrdersListRow> findAll();

  void create( OrdersListRow order );
}
