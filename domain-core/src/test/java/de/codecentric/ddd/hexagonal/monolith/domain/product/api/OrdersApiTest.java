package de.codecentric.ddd.hexagonal.monolith.domain.product.api;

import de.codecentric.ddd.hexagonal.monolith.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.monolith.domain.order.impl.OrdersApiImpl;
import de.codecentric.ddd.hexagonal.monolith.persistence.OrderRepositoryInMemory;
import jdk.jfr.Description;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

public class OrdersApiTest {

  public static final UUID UUID = java.util.UUID.fromString( "572e35e4-7be4-4c4a-bc49-32acd9aa09bc" );

  @Nested
  @Description( "Given no orders have been taken" )
  public class GivenNoOrders {
    private OrdersApi api;
    @BeforeEach
    void setUp() {
      api = new OrdersApiImpl( new OrderRepositoryInMemory());
    }

    @Nested
    @Description( "when an order is created" )
    class WhenAnOrderIsCreated {
      private Order order;

      @BeforeEach
      void setUp() {
        order = new Order( UUID, Collections.emptyList() );
        api.createOrder( order );
      }

      @Test
      @Description("should return a list with the new order")
      void shouldReturnListWithNewOrder() {
        assertThat(api.getOrders()).isEqualTo( Collections.singletonList( order ) );
      }
    }

    @AfterEach
    void tearDown() {
      api = null;
    }
  }
}
