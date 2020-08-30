package de.codecentric.ddd.hexagonal.order.persistence;

import de.codecentric.ddd.hexagonal.domain.order.api.OrdersListRow;
import de.codecentric.ddd.hexagonal.domain.product.api.OrdersListRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrdersListRepositoryInMemory implements OrdersListRepository {
  private final HashMap<UUID, OrdersListRow> orders;

  public OrdersListRepositoryInMemory() {
    this.orders = new HashMap<>();
  }

  @Override public List<OrdersListRow> findAll() {
    return orders.values().stream().sorted( ( OrdersListRow o, OrdersListRow p ) -> {
      final LocalDateTime oTime = LocalDateTime.parse( o.getTimestamp(), DATE_TIME_FORMATTER );
      final LocalDateTime pTime = LocalDateTime.parse( p.getTimestamp(), DATE_TIME_FORMATTER );
      return oTime.isAfter( pTime ) ? -1 : oTime.isBefore( pTime ) ? 1 : 0;

    } ).collect( Collectors.toUnmodifiableList() );
  }

  @Override public void create( final OrdersListRow order ) {
    final OrdersListRow saved = new OrdersListRow( order.getId(),
                                   order.getTotal(),
                                   order.getPositions(),
                                   order.getTimestamp() != null
                                   ? order.getTimestamp()
                                   : LocalDateTime.now().format( DATE_TIME_FORMATTER ) );
    orders.put( order.getId(), saved );

  }
}
