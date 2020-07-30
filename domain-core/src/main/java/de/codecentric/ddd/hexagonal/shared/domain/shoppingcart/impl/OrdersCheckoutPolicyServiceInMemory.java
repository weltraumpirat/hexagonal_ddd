package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.shared.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.shared.domain.order.api.OrderPosition;
import de.codecentric.ddd.hexagonal.shared.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.OrdersCheckoutPolicyService;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCartItem;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.*;
import java.util.stream.Collectors;

public class OrdersCheckoutPolicyServiceInMemory implements OrdersCheckoutPolicyService {
  private final OrdersApi ordersApi;

  public OrdersCheckoutPolicyServiceInMemory( final OrdersApi ordersApi ) {
    this.ordersApi = ordersApi;
  }

  @Override public void invoke( List<ShoppingCartItem> items ) {
    if( items.size()>0 ) {
      final Map<String, OrderPosition> positions = new HashMap<>();
      items.forEach( ( item -> addOne( item, positions ) ) );
      final List<OrderPosition> sortedPositions =
        positions.values().stream()
          .sorted( ( o1, o2 ) -> o1.getItemName().compareToIgnoreCase( o2.getItemName() ) )
          .collect( Collectors.toUnmodifiableList() );

      final Money total = sortedPositions.stream()
                            .map( OrderPosition::getCombinedPrice )
                            .reduce( ( Money price, Money acc) -> acc != null? acc.plus( price ): price )
        .orElse( Money.zero( CurrencyUnit.EUR ) );
      final Order order = new Order( UUID.randomUUID(), total, sortedPositions, null );
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
    return Money.zero( price.getCurrencyUnit());
  }
}
