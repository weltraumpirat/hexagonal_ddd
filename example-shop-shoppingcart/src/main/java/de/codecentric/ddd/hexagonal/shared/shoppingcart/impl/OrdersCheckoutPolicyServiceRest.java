package de.codecentric.ddd.hexagonal.shared.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.OrdersCheckoutPolicyService;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrdersCheckoutPolicyServiceRest implements OrdersCheckoutPolicyService {
  private final RestTemplate template;

  public OrdersCheckoutPolicyServiceRest( final RestTemplate template ) {
    this.template = template;
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
      template.postForObject( "http://example-shop-order:8080/api/order", order, Order.class);
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
