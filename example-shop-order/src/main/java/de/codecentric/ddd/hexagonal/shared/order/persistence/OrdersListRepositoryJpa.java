package de.codecentric.ddd.hexagonal.shared.order.persistence;

import de.codecentric.ddd.hexagonal.domain.order.api.OrderPosition;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersListRow;
import de.codecentric.ddd.hexagonal.domain.product.api.OrdersListRepository;
import static de.codecentric.ddd.hexagonal.shared.config.json.MoneyMapper.toMoney;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import org.joda.money.Money;

import java.time.LocalDateTime;
import java.util.*;

public class OrdersListRepositoryJpa implements OrdersListRepository {
  private final OrdersListCrudRepository          crudRepository;
  private final OrdersListPositionsCrudRepository positionsCrudRepository;

  public OrdersListRepositoryJpa(
    final OrdersListCrudRepository crudRepository,
    final OrdersListPositionsCrudRepository positionsCrudRepository ) {
    this.crudRepository = crudRepository;
    this.positionsCrudRepository = positionsCrudRepository;
  }

  @Override public List<OrdersListRow> findAll() {
    final Map<UUID, List<OrderPosition>> orders = new HashMap<>();
    positionsCrudRepository.findAll().forEach( e -> {
      final UUID orderId = e.getOrderId();
      final Money price = toMoney( e.getSinglePrice() );
      final Money combinedPrice = toMoney( e.getCombinedPrice() );

      final List<OrderPosition> positions = orders.getOrDefault( orderId, new ArrayList<>() );
      positions.add( new OrderPosition( e.getId(), e.getItemName(), e.getCount(), price, combinedPrice ) );
      orders.put( orderId, positions );
    } );
    return crudRepository.findAll().stream()
                          .map( o -> new OrdersListRow( o.getId(),
                                                toMoney( o.getTotal() ),
                                                orders.get( o.getId() ),
                                                DATE_TIME_FORMATTER.format( o.getTimestamp() ) ) )
                          .collect( toUnmodifiableList() );
  }

  @Override public void create( final OrdersListRow order ) {
    crudRepository.save( new OrdersListRowEntity( order.getId(),
                                        order.getTotal().toString(),
                                        LocalDateTime.parse( order.getTimestamp(), DATE_TIME_FORMATTER ) ) );
    final List<OrdersListRowPositionEntity> entities = order.getPositions().stream().map( p -> {
      OrdersListRowPositionEntity entity = new OrdersListRowPositionEntity();
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
    positionsCrudRepository.saveAll( entities );
  }
}
