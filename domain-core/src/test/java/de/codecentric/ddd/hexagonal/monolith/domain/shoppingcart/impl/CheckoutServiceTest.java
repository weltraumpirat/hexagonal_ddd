package de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.monolith.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrderPosition;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.monolith.domain.order.impl.OrdersApiImpl;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.monolith.product.persistence.OrderRepositoryInMemory;
import jdk.jfr.Description;
import static org.assertj.core.api.Assertions.assertThat;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class CheckoutServiceTest {
  private CheckoutService service;
  private OrdersApi       ordersApi;

  @BeforeEach
  void setUp() {
    ordersApi = new OrdersApiImpl( new OrderRepositoryInMemory() );
    service = new CheckoutService( ordersApi );
  }

  @Nested
  @Description( "When a shopping cart is checked out without items" )
  class WhenAShoppingCartIsCheckedOutWithNoItems {
    @Test
    @Description( "should not create an order" )
    void shouldNotCreateAnOrder() {
      service.checkOut( Collections.emptyList() );
      assertThat( ordersApi.getOrders() ).isEmpty();
    }
  }

  @Nested
  @Description( "When a shopping cart is checked out with a single item" )
  class WhenAShoppingCartIsCheckedOutWithSingleItem {
    @Test
    @Description( "should create an order with one position" )
    void shouldCreateAnOrderWithOnePosition() {
      final Money singlePrice = Money.of( CurrencyUnit.of( "EUR" ), new BigDecimal( "1" ) );
      service.checkOut( Collections.singletonList(
        new ShoppingCartItem( UUID.randomUUID(),
                              "Whole Milk, 0.5l Carton",
                              singlePrice ) ) );
      final List<Order> orders = ordersApi.getOrders();
      assertThat( orders ).hasSize( 1 );
      final Order order = orders.get( 0 );
      final OrderPosition expectedPosition =
        new OrderPosition( order.getPositions().get( 0 ).getId(), "Whole Milk, 0.5l Carton", 1, singlePrice, singlePrice );
      assertThat( order ).isEqualTo( new Order( order.getId(), Collections.singletonList( expectedPosition ) ) );
    }
  }

  @Nested
  @Description( "When a shopping cart is checked out with two items of the same product" )
  class WhenAShoppingCartIsCheckedOutWithTwoItemsOfSameProduct {
    @Test
    @Description( "should create an order with one position and calculated price" )
    void shouldCreateAnOrderWithOnePosition() {
      final Money singlePrice = Money.of( CurrencyUnit.of( "EUR" ), new BigDecimal( "1" ) );
      final ShoppingCartItem item = new ShoppingCartItem( UUID.randomUUID(),
                                                                "Whole Milk, 0.5l Carton",
                                                                singlePrice );
      service.checkOut( Arrays.asList( item, item ) );
      final List<Order> orders = ordersApi.getOrders();
      assertThat( orders ).hasSize( 1 );
      final Order order = orders.get( 0 );
      final OrderPosition expectedPosition =
        new OrderPosition( order.getPositions().get( 0 ).getId(), "Whole Milk, 0.5l Carton", 2, singlePrice, singlePrice.multipliedBy( 2 ) );
      assertThat( order ).isEqualTo( new Order( order.getId(), Collections.singletonList( expectedPosition ) ) );
    }
  }

  @Nested
  @Description( "When a shopping cart is checked out with two items of different products" )
  class WhenAShoppingCartIsCheckedOutWithTwoItemsOfDifferentProducts {
    @Test
    @Description( "should create an order with two positions" )
    void shouldCreateAnOrderWithOnePosition() {
      final Money singlePrice = Money.of( CurrencyUnit.of( "EUR" ), new BigDecimal( "1" ) );
      final Money singlePrice2 = Money.of( CurrencyUnit.of( "EUR" ), new BigDecimal( "1.69" ) );
      final ShoppingCartItem item = new ShoppingCartItem( UUID.randomUUID(),
                                                          "Whole Milk, 0.5l Carton",
                                                          singlePrice );
      final ShoppingCartItem item2 = new ShoppingCartItem( UUID.randomUUID(),
                                                          "Whole Milk, 1l Carton",
                                                           singlePrice2);
      service.checkOut( Arrays.asList( item, item2 ) );
      final List<Order> orders = ordersApi.getOrders();
      assertThat( orders ).hasSize( 1 );
      final Order order = orders.get( 0 );
      final OrderPosition expectedPosition =
        new OrderPosition(order.getPositions().get(0).getId(), "Whole Milk, 0.5l Carton", 1, singlePrice, singlePrice );
      final OrderPosition expectedPosition2 =
        new OrderPosition( order.getPositions().get(1).getId(), "Whole Milk, 1l Carton", 1, singlePrice2, singlePrice2 );
      assertThat( order ).isEqualTo( new Order( order.getId(), Arrays.asList(expectedPosition, expectedPosition2) ) );
    }
  }
}
