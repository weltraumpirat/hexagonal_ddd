package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.shared.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.shared.domain.order.api.OrderPosition;
import de.codecentric.ddd.hexagonal.shared.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.OrdersCheckoutPolicyService;
import de.codecentric.ddd.hexagonal.shared.domain.order.impl.OrdersApiImpl;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.shared.product.persistence.OrderRepositoryInMemory;
import jdk.jfr.Description;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.money.CurrencyUnit.EUR;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class OrdersCheckoutPolicyServiceTest {
  private OrdersCheckoutPolicyService service;
  private OrdersApi                   ordersApi;

  @BeforeEach
  void setUp() {
    ordersApi = new OrdersApiImpl( new OrderRepositoryInMemory() );
    service = new OrdersCheckoutPolicyServiceInMemory( ordersApi );
  }

  @Nested
  @Description( "When a shopping cart is checked out without items" )
  class WhenAShoppingCartIsCheckedOutWithNoItems {
    @Test
    @Description( "should not create an order" )
    void shouldNotCreateAnOrder() {
      service.invoke( Collections.emptyList() );
      assertThat( ordersApi.getOrders() ).isEmpty();
    }
  }

  @Nested
  @Description( "When a shopping cart is checked out with a single item" )
  class WhenAShoppingCartIsCheckedOutWithSingleItem {
    @Test
    @Description( "should create an order with one position" )
    void shouldCreateAnOrderWithOnePosition() {
      final Money singlePrice = Money.of( EUR, new BigDecimal( "1" ) );
      service.invoke( Collections.singletonList(
        new ShoppingCartItem( UUID.randomUUID(),
                              "Whole Milk, 0.5l Carton",
                              singlePrice ) ) );
      final List<Order> orders = ordersApi.getOrders();
      assertThat( orders ).hasSize( 1 );
      final Order order = orders.get( 0 );
      final OrderPosition expectedPosition =
        new OrderPosition( order.getPositions().get( 0 ).getId(), "Whole Milk, 0.5l Carton", 1, singlePrice, singlePrice );
      assertThat( order ).isEqualTo( new Order( order.getId(), singlePrice, Collections.singletonList( expectedPosition ), order.getTimestamp() ) );
    }
  }

  @Nested
  @Description( "When a shopping cart is checked out with two items of the same product" )
  class WhenAShoppingCartIsCheckedOutWithTwoItemsOfSameProduct {
    @Test
    @Description( "should create an order with one position and calculated price" )
    void shouldCreateAnOrderWithOnePosition() {
      final Money singlePrice = Money.of( EUR, new BigDecimal( "1" ) );
      final ShoppingCartItem item = new ShoppingCartItem( UUID.randomUUID(),
                                                                "Whole Milk, 0.5l Carton",
                                                                singlePrice );
      service.invoke( Arrays.asList( item, item ) );
      final List<Order> orders = ordersApi.getOrders();
      assertThat( orders ).hasSize( 1 );
      final Order order = orders.get( 0 );
      final OrderPosition expectedPosition =
        new OrderPosition( order.getPositions().get( 0 ).getId(), "Whole Milk, 0.5l Carton", 2, singlePrice, singlePrice.multipliedBy( 2 ) );
      assertThat( order ).isEqualTo( new Order( order.getId(), Money.of(EUR, new BigDecimal( "2" )), Collections.singletonList( expectedPosition ), order.getTimestamp() ) );
    }
  }

  @Nested
  @Description( "When a shopping cart is checked out with two items of different products" )
  class WhenAShoppingCartIsCheckedOutWithTwoItemsOfDifferentProducts {
    @Test
    @Description( "should create an order with two positions" )
    void shouldCreateAnOrderWithTwoPositions() {
      final Money singlePrice = Money.of( EUR, new BigDecimal( "1" ) );
      final Money singlePrice2 = Money.of( EUR, new BigDecimal( "1.69" ) );
      final ShoppingCartItem item = new ShoppingCartItem( UUID.randomUUID(),
                                                          "Whole Milk, 0.5l Carton",
                                                          singlePrice );
      final ShoppingCartItem item2 = new ShoppingCartItem( UUID.randomUUID(),
                                                          "Whole Milk, 1l Carton",
                                                           singlePrice2);
      service.invoke( Arrays.asList( item, item2 ) );
      final List<Order> orders = ordersApi.getOrders();
      assertThat( orders ).hasSize( 1 );
      final Order order = orders.get( 0 );
      final OrderPosition expectedPosition =
        new OrderPosition(order.getPositions().get(0).getId(), "Whole Milk, 0.5l Carton", 1, singlePrice, singlePrice );
      final OrderPosition expectedPosition2 =
        new OrderPosition( order.getPositions().get(1).getId(), "Whole Milk, 1l Carton", 1, singlePrice2, singlePrice2 );
      assertThat( order ).isEqualTo( new Order( order.getId(), Money.of(EUR, new BigDecimal( "2.69" )), Arrays.asList(expectedPosition, expectedPosition2), order.getTimestamp() ) );
    }
  }
}
