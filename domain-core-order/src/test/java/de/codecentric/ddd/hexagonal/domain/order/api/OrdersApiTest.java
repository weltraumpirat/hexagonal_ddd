package de.codecentric.ddd.hexagonal.domain.order.api;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.common.messaging.MessagebusLocal;
import de.codecentric.ddd.hexagonal.domain.common.messaging.TransactionFactory;
import de.codecentric.ddd.hexagonal.domain.order.impl.OrdersFixture;
import de.codecentric.ddd.hexagonal.domain.order.impl.OrdersApiImpl;
import de.codecentric.ddd.hexagonal.domain.order.impl.OrdersListReadModel;
import de.codecentric.ddd.hexagonal.domain.order.messaging.CreateOrderCommand;
import static de.codecentric.ddd.hexagonal.domain.product.api.OrdersListRepository.DATE_TIME_FORMATTER;
import de.codecentric.ddd.hexagonal.order.persistence.OrderRepositoryInMemory;
import de.codecentric.ddd.hexagonal.order.persistence.OrdersListRepositoryInMemory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.money.CurrencyUnit.EUR;
import org.joda.money.Money;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class OrdersApiTest {

  public static final UUID UUID = java.util.UUID.fromString( "572e35e4-7be4-4c4a-bc49-32acd9aa09bc" );

  @Nested
  @DisplayName( "Given no orders have been taken" )
  public class GivenNoOrders {
    private       OrdersApi  api;
    private final Messagebus eventbus   = new MessagebusLocal();
    private final Messagebus commandbus = new MessagebusLocal();

    @BeforeEach
    void setUp() {
      final OrdersFixture ordersFixture = new OrdersFixture( new OrderRepositoryInMemory(), eventbus, commandbus );
      final OrdersListReadModel ordersListReadModel = new OrdersListReadModel(
        new OrdersListRepositoryInMemory(), eventbus );
      final TransactionFactory transactionFactory = new TransactionFactory( eventbus, commandbus );

      api = new OrdersApiImpl( ordersFixture, ordersListReadModel, transactionFactory );
    }

    @Nested
    @DisplayName( "when an order is created" )
    class WhenAnOrderIsCreated {
      private OrdersListRow listRow;

      @BeforeEach
      void setUp() throws ExecutionException, InterruptedException {
        final String time = LocalDateTime.now().format( DATE_TIME_FORMATTER );
        listRow = new OrdersListRow( UUID, Money.zero( EUR ), Collections.emptyList(), time );
        final Order order = new Order( UUID, Money.zero( EUR ), Collections.emptyList(), time );
        api.createOrder( order ).get();
      }

      @Test
      @DisplayName( "should return a list with the new order" )
      void shouldReturnListWithNewOrder() {
        assertThat( api.getOrders() ).isEqualTo( Collections.singletonList( listRow ) );
      }


    }

    @Nested
    @DisplayName( "when order creation fails" )
    class WhenOrderCreationFails {
      @BeforeEach
      void setUp() {
        commandbus.unregisterAll( CreateOrderCommand.class );

        commandbus.register( CreateOrderCommand.class, ( final Message<?> command ) -> {
          throw new RuntimeException( "Failed." );
        } );

        final String time = LocalDateTime.now().format( DATE_TIME_FORMATTER );
        final Order order = new Order( UUID, Money.zero( EUR ), Collections.emptyList(), time );
        api.createOrder( order ).complete( null );
      }

      @Test
      @DisplayName( "should return empty list" )
      void shouldReturnListWithNewOrder() {
        assertThat( api.getOrders() ).isEqualTo( Collections.emptyList() );
      }
    }

    @AfterEach
    void tearDown() {
      api = null;
    }
  }


}
