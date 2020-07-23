package de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.monolith.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrderPosition;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartItem;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class CheckoutService {
  private final OrdersApi ordersApi;

  public CheckoutService( final OrdersApi ordersApi ) {
    this.ordersApi = ordersApi;
  }

  public void checkOut( List<ShoppingCartItem> items ) {
    if( items.size()>0 ) {
      final Map<String, OrderPosition> positions = new HashMap<>();
      items.forEach( ( item -> addOne( item, positions ) ) );
      final Order order =
        new Order( UUID.randomUUID(), positions.values().stream().sorted((o1, o2) -> o1.getItemName().compareToIgnoreCase( o2.getItemName() ) ).collect( Collectors.toUnmodifiableList() ) );
      ordersApi.createOrder( order );
    }
  }

  private void addOne( final ShoppingCartItem item, final Map<String, OrderPosition> positions ) {
    final String label = item.getLabel();
    final Money price = item.getPrice();
    final OrderPosition defaultPosition = new OrderPosition( UUID.randomUUID(), label, 0, price, zeroPrice( price ) );
    final OrderPosition position = positions.getOrDefault( label, defaultPosition );
    final Money combinedPrice = position.getCombinedPrice().plus( price );
    positions.put( label, new OrderPosition( position.getId(), label, position.getCount()+1, price, combinedPrice ) );
  }

  private Money zeroPrice( final Money price ) {
    return Money.of( price.getCurrencyUnit(), new BigDecimal( "0" ) );
  }
}
