package de.codecentric.ddd.hexagonal.shared.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.OrdersCheckoutPolicyService;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.money.CurrencyUnit.EUR;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

class OrdersCheckoutPolicyServiceTest {
  private OrdersCheckoutPolicyService service;
  private RestTemplate                restTemplate;

  @BeforeEach
  void setUp() {
    restTemplate = mock( RestTemplate.class );
    service = new OrdersCheckoutPolicyServiceRest( restTemplate );
  }

  @Nested
  @DisplayName( "When a shopping cart is checked out without items" )
  class WhenAShoppingCartIsCheckedOutWithNoItems {
    @Test
    @DisplayName( "should not create an order" )
    void shouldNotCreateAnOrder() {
      service.invoke( Collections.emptyList() );
      verify( restTemplate, never() ).patchForObject( any(), any(), any() );
    }
  }

  @Nested
  @DisplayName( "When a shopping cart is checked out with a single item" )
  class WhenAShoppingCartIsCheckedOutWithSingleItem {
    @Test
    @DisplayName( "should create an order with one position" )
    void shouldCreateAnOrderWithOnePosition() {
      final Money singlePrice = Money.of( EUR, new BigDecimal( "1" ) );
      service.invoke( Collections.singletonList(
        new ShoppingCartItem( UUID.randomUUID(),
                              "Whole Milk, 0.5l Carton",
                              singlePrice ) ) );
      final ArgumentCaptor<Order> captor = ArgumentCaptor.forClass( Order.class );
      //noinspection unchecked
      verify( restTemplate, times( 1 ) ).postForObject( any( String.class ), captor.capture(), any( Class.class ) );
      final Order order = captor.getValue();
      final OrderPosition expectedPosition =
        new OrderPosition( order.getPositions().get( 0 ).getId(), "Whole Milk, 0.5l Carton", 1, singlePrice,
                           singlePrice );
      assertThat( order ).isEqualTo(
        new Order( order.getId(), singlePrice, Collections.singletonList( expectedPosition ), order.getTimestamp() ) );
    }
  }

  @Nested
  @DisplayName( "When a shopping cart is checked out with two items of the same product" )
  class WhenAShoppingCartIsCheckedOutWithTwoItemsOfSameProduct {
    @Test
    @DisplayName( "should create an order with one position and calculated price" )
    void shouldCreateAnOrderWithOnePosition() {
      final Money singlePrice = Money.of( EUR, new BigDecimal( "1" ) );
      final ShoppingCartItem item = new ShoppingCartItem( UUID.randomUUID(),
                                                          "Whole Milk, 0.5l Carton",
                                                          singlePrice );
      service.invoke( Arrays.asList( item, item ) );
      final ArgumentCaptor<Order> captor = ArgumentCaptor.forClass( Order.class );
      //noinspection unchecked
      verify( restTemplate, times( 1 ) ).postForObject( any( String.class ), captor.capture(), any( Class.class ) );
      final Order order = captor.getValue();
      final OrderPosition expectedPosition =
        new OrderPosition( order.getPositions().get( 0 ).getId(), "Whole Milk, 0.5l Carton", 2, singlePrice,
                           singlePrice.multipliedBy( 2 ) );
      assertThat( order ).isEqualTo(
        new Order( order.getId(), Money.of( EUR, new BigDecimal( "2" ) ), Collections.singletonList( expectedPosition ),
                   order.getTimestamp() ) );
    }
  }

  @Nested
  @DisplayName( "When a shopping cart is checked out with two items of different products" )
  class WhenAShoppingCartIsCheckedOutWithTwoItemsOfDifferentProducts {
    @Test
    @DisplayName( "should create an order with two positions" )
    void shouldCreateAnOrderWithTwoPositions() {
      final Money singlePrice = Money.of( EUR, new BigDecimal( "1" ) );
      final Money singlePrice2 = Money.of( EUR, new BigDecimal( "1.69" ) );
      final ShoppingCartItem item = new ShoppingCartItem( UUID.randomUUID(),
                                                          "Whole Milk, 0.5l Carton",
                                                          singlePrice );
      final ShoppingCartItem item2 = new ShoppingCartItem( UUID.randomUUID(),
                                                           "Whole Milk, 1l Carton",
                                                           singlePrice2 );
      service.invoke( Arrays.asList( item, item2 ) );
      final ArgumentCaptor<Order> captor = ArgumentCaptor.forClass( Order.class );
      //noinspection unchecked
      verify( restTemplate, times( 1 ) ).postForObject( any( String.class ), captor.capture(), any( Class.class ) );
      final Order order = captor.getValue();
      final OrderPosition expectedPosition =
        new OrderPosition( order.getPositions().get( 0 ).getId(), "Whole Milk, 0.5l Carton", 1, singlePrice,
                           singlePrice );
      final OrderPosition expectedPosition2 =
        new OrderPosition( order.getPositions().get( 1 ).getId(), "Whole Milk, 1l Carton", 1, singlePrice2,
                           singlePrice2 );
      assertThat( order ).isEqualTo( new Order( order.getId(), Money.of( EUR, new BigDecimal( "2.69" ) ),
                                                Arrays.asList( expectedPosition, expectedPosition2 ),
                                                order.getTimestamp() ) );
    }
  }
}
