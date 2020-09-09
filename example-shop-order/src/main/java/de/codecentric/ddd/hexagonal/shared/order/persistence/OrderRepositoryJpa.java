package de.codecentric.ddd.hexagonal.shared.order.persistence;

import de.codecentric.ddd.hexagonal.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.domain.order.api.OrderPosition;
import de.codecentric.ddd.hexagonal.domain.order.api.OrderRepository;
import static de.codecentric.ddd.hexagonal.shared.config.json.MoneyMapper.toMoney;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import org.joda.money.Money;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class OrderRepositoryJpa implements OrderRepository {
  public static final DateTimeFormatter           DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  private final       OrderPositionCrudRepository jpaPositionsRepo;
  private final       OrderCrudRepository         jpaOrderRepo;

  public OrderRepositoryJpa(
    final OrderCrudRepository jpaOrderRepo,
    final OrderPositionCrudRepository jpaPositionsRepo ) {
    this.jpaOrderRepo = jpaOrderRepo;
    this.jpaPositionsRepo = jpaPositionsRepo;
  }

  @Override public void create( final Order order ) {
    jpaOrderRepo.save( new OrderEntity( order.getId(),
                                        order.getTotal().toString(),
                                        LocalDateTime.parse( order.getTimestamp(), DATE_TIME_FORMATTER ) ) );
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
    } ).collect( toList() );
    jpaPositionsRepo.saveAll( entities );
  }

  @Override public List<Order> findAll() {
    final Map<UUID, List<OrderPosition>> orders = new HashMap<>();
    jpaPositionsRepo.findAll().forEach( e -> {
      final UUID orderId = e.getOrderId();
      final Money price = toMoney( e.getSinglePrice() );
      final Money combinedPrice = toMoney( e.getCombinedPrice() );

      final List<OrderPosition> positions = orders.getOrDefault( orderId, new ArrayList<>() );
      positions.add( new OrderPosition( e.getId(), e.getItemName(), e.getCount(), price, combinedPrice ) );
      orders.put( orderId, positions );
    } );

    return StreamSupport.stream( jpaOrderRepo.findAll().spliterator(), false )
             .map( o -> new Order( o.getId(), toMoney( o.getTotal() ), orders.get( o.getId() ),
                                   DATE_TIME_FORMATTER.format( o.getTimestamp() ) ) )
             .collect( toUnmodifiableList() );
  }
}
