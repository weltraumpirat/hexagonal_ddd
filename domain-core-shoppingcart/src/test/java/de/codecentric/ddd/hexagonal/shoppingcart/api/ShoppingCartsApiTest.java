package de.codecentric.ddd.hexagonal.shoppingcart.api;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.common.messaging.MessagebusLocal;
import de.codecentric.ddd.hexagonal.domain.common.messaging.TransactionFactory;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.*;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.*;
import de.codecentric.ddd.hexagonal.shoppingcart.persistence.ShoppingCartItemsInfoRepositoryInMemory;
import de.codecentric.ddd.hexagonal.shoppingcart.persistence.ShoppingCartListRowRepositoryInMemory;
import de.codecentric.ddd.hexagonal.shoppingcart.persistence.ShoppingCartRepositoryInMemory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ShoppingCartsApiTest {

  public static final UUID                               ITEM_ID = UUID.randomUUID();
  private             OrdersCheckoutPolicyService        ordersCheckoutPolicyService;
  private             ShoppingCartsCheckoutPolicyService shoppingCartsCheckoutPolicyService;
  private             ProductValidationService           validationService;

  @BeforeEach
  void setUp() {
    validationService = mock( ProductValidationService.class );
    ordersCheckoutPolicyService = mock( OrdersCheckoutPolicyService.class );
  }

  @Nested
  @DisplayName( "Given no prior shoppingcarts" )
  class GivenNoShoppingCarts {
    private ShoppingCartsApi api;

    @BeforeEach
    void setUp() {
      final Messagebus eventbus = new MessagebusLocal();
      final Messagebus commandbus = new MessagebusLocal();
      final TransactionFactory factory = new TransactionFactory( eventbus, commandbus );
      shoppingCartsCheckoutPolicyService = new ShoppingCartsCheckoutPolicyServiceInMemory( factory );
      final ShoppingCartRepositoryInMemory repository = new ShoppingCartRepositoryInMemory();
      ShoppingCartFixture shoppingCartFixture =
        new ShoppingCartFixture( repository,
                                 validationService,
                                 ordersCheckoutPolicyService,
                                 shoppingCartsCheckoutPolicyService,
                                 eventbus,
                                 commandbus
        );
      this.api = new ShoppingCartsApiImpl(
        shoppingCartFixture,
        new ShoppingCartListReadModel( new ShoppingCartListRowRepositoryInMemory(), eventbus ),
        new ShoppingCartItemsReadModel( new ShoppingCartItemsInfoRepositoryInMemory(), eventbus ), factory );
    }

    @Nested
    @DisplayName( "when an empty shopping cart is created" )
    class WhenEmptyShoppingCartIsCreated {
      private UUID cartId;

      @BeforeEach
      void setUp() throws ExecutionException, InterruptedException {
        cartId = api.createEmptyShoppingCart().get();
      }

      @Test
      void shouldReturnListWithShoppingCart() {
        assertThat( api.getShoppingCarts().get( 0 ).getId() ).isEqualTo( cartId );
      }
    }

    @AfterEach
    void tearDown() {
      api = null;
    }
  }

  @Nested
  @DisplayName( "Given an existing shopping cart" )
  class GivenAnExistingShoppingCart {
    private ShoppingCartsApi api;
    private UUID             cartId;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
      final Messagebus eventbus = new MessagebusLocal();
      final Messagebus commandbus = new MessagebusLocal();
      final TransactionFactory factory = new TransactionFactory( eventbus, commandbus );
      ordersCheckoutPolicyService = mock( OrdersCheckoutPolicyService.class );
      shoppingCartsCheckoutPolicyService = new ShoppingCartsCheckoutPolicyServiceInMemory( factory );
      final ShoppingCartRepositoryInMemory repository = new ShoppingCartRepositoryInMemory();
      ShoppingCartFixture shoppingCartFixture =
        new ShoppingCartFixture( repository, validationService, ordersCheckoutPolicyService,
                                 shoppingCartsCheckoutPolicyService, eventbus, commandbus
        );
      api = new ShoppingCartsApiImpl(
        shoppingCartFixture,
        new ShoppingCartListReadModel( new ShoppingCartListRowRepositoryInMemory(), eventbus ),
        new ShoppingCartItemsReadModel( new ShoppingCartItemsInfoRepositoryInMemory(), eventbus ),
        factory );
      cartId = api.createEmptyShoppingCart().get();
    }

    @Nested
    @DisplayName( "when a valid item is added" )
    class WhenAValidItemIsAdded {

      private ShoppingCartItem item;

      @BeforeEach
      void setUp() throws ExecutionException, InterruptedException {
        final Money singlePrice = Money.of( CurrencyUnit.of( "EUR" ), new BigDecimal( "1" ) );
        item = new ShoppingCartItem( ITEM_ID,
                                     "Whole Milk, Carton 0.5l",
                                     singlePrice );
        api.addItemToShoppingCart( cartId, item ).get();
      }

      @Test
      void shouldContainTheItem() {
        assertThat( api.getShoppingCartItems( cartId ).getItems() ).isEqualTo( Collections.singletonList( item ) );
      }
    }

    @Nested
    @DisplayName( "when an invalid item is added" )
    class WhenAnInvalidItemIsAdded {

      private ShoppingCartItem item;

      @BeforeEach
      void setUp() {
        doThrow( NoSuchElementException.class ).when( validationService ).validate( any( ShoppingCartItem.class ) );
        final Money singlePrice = Money.of( CurrencyUnit.of( "EUR" ), new BigDecimal( "1" ) );
        item = new ShoppingCartItem( ITEM_ID,
                                     "Unknown product label",
                                     singlePrice );
      }

      @Test
      void shouldThrow() {
        assertThatThrownBy( () -> api.addItemToShoppingCart( cartId, item ).get() );
      }
    }

    @Nested
    @DisplayName( "and it contains an item" )
    class AndAnItemExists {

      private ShoppingCartItem item;

      @BeforeEach
      void setUp() throws ExecutionException, InterruptedException {
        final Money singlePrice = Money.of( CurrencyUnit.of( "EUR" ), new BigDecimal( "1" ) );
        item = new ShoppingCartItem( ITEM_ID,
                                     "Whole Milk, Carton 0.5l",
                                     singlePrice );
        api.addItemToShoppingCart( cartId, item ).get();
      }

      @Nested
      @DisplayName( "when the item is removed" )
      class WhenTheItemIsRemoved {

        @BeforeEach
        void setUp() throws ExecutionException, InterruptedException {
          api.removeItemFromShoppingCart( cartId, item.getId() ).get();
        }

        @Test
        void shouldBeEmpty() {
          assertThat( api.getShoppingCartItems( cartId ).getCount() ).isEqualTo( 0 );
        }
      }

      @Nested
      @DisplayName( "when the cart is checked out" )
      class WhenTheCartIsCheckedOut {
        private UUID newCartId;

        @BeforeEach
        void setUp() throws ExecutionException, InterruptedException {
          newCartId = api.checkOut( cartId ).get();
        }

        @Test
        @DisplayName( "the cart should no longer exist" )
        void cartShouldNoLongerExist() {
          assertThat( api.getShoppingCarts().stream().filter( cart -> cart.getId().equals( cartId ) ).count() )
            .isEqualTo( 0 );
        }

        @Test
        @DisplayName( "an order should be created" )
        void anOrderShouldBeCreated() {
          verify( ordersCheckoutPolicyService, times( 1 ) ).invoke( Collections.singletonList( item ) );
        }

        @Test
        @DisplayName( "a new empty cart should be created" )
        void anEmptyCartShouldBeCreated() {
          assertThat( api.getShoppingCarts().get( 0 ).getId() ).isEqualTo( newCartId );
        }
      }
    }
  }
}
