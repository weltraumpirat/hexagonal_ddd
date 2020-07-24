package de.codecentric.ddd.hexagonal.monolith.order.persistence;

import static de.codecentric.ddd.hexagonal.monolith.config.json.MoneyMapper.toMoney;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrderPosition;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrderRepository;
import org.joda.money.Money;

import java.util.*;
import java.util.stream.Collectors;

public class OrderRepositoryJpa implements OrderRepository {
  private final OrderPositionCrudRepository jpaRepo;

  public OrderRepositoryJpa( final OrderPositionCrudRepository jpaRepo ) {
    this.jpaRepo = jpaRepo;
  }

  @Override public void create( final Order order ) {
    final List<OrderPositionEntity> entities = order.getPositions().stream().map( p -> {
      OrderPositionEntity entity = new OrderPositionEntity();
      entity.setId( p.getId() );
      entity.setOrderId( order.getId() );
      entity.setCount( p.getCount() );
      entity.setItemName( p.getItemName() );
      final Money single = p.getSinglePrice();
      entity.setSinglePrice( single.getCurrencyUnit()+" "+single.getAmount().toString() );
      final Money combined = p.getCombinedPrice();
      entity.setCombinedPrice( combined.getCurrencyUnit()+" "+combined.getAmount().toString() );
      return entity;
    } ).collect( Collectors.toList() );
    jpaRepo.saveAll( entities );
  }

  @Override public List<Order> findAll() {
    Map<UUID, Order> orders = new HashMap<>();
    jpaRepo.findAll().forEach( e -> {
      final UUID orderId = e.getOrderId();
      final Order prev = orders.get( orderId );
      final List<OrderPosition> positions = prev != null ? prev.getPositions() : new ArrayList<>();
      positions.add( new OrderPosition( e.getId(), e.getItemName(), e.getCount(), toMoney( e.getSinglePrice() ),
                                        toMoney( e.getCombinedPrice() ) ) );
      final Order next = new Order( orderId, positions );
      orders.put( orderId, next );
    } );
    return orders.values().stream()
             .collect( Collectors.toUnmodifiableList() );
  }
}
