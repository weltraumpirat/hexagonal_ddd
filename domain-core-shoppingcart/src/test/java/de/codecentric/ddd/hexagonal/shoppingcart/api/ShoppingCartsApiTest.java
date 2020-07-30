package de.codecentric.ddd.hexagonal.shoppingcart.api;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.OrdersCheckoutPolicyService;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ProductValidationService;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartsApi;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartsApiImpl;
import de.codecentric.ddd.hexagonal.shoppingcart.persistence.ShoppingCartRepositoryInMemory;
import jdk.jfr.Description;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ShoppingCartsApiTest {

  public static final UUID                        ITEM_ID = UUID.randomUUID();
  private             OrdersCheckoutPolicyService ordersCheckoutPolicyService;
  private             ProductValidationService    validationService;

  @BeforeEach
  void setUp() {
    validationService = mock( ProductValidationService.class );
    ordersCheckoutPolicyService = mock( OrdersCheckoutPolicyService.class );
  }

  @Nested
  @Description( "Given no prior shoppingcarts" )
  class GivenNoShoppingCarts {
    private ShoppingCartsApi api;

    @BeforeEach
    void setUp() {
      this.api = new ShoppingCartsApiImpl( ordersCheckoutPolicyService, validationService,
                                           new ShoppingCartRepositoryInMemory() );
    }

    @Nested
    @Description( "when an empty shopping cart is created" )
    class WhenEmptyShoppingCartIsCreated {
      private UUID cartId;

      @BeforeEach
      void setUp() {
        cartId = api.createEmptyShoppingCart();
      }

      @Test
      void shouldReturnListWithShoppingCart() {
        assertThat( api.getShoppingCartById( cartId ).getId() ).isEqualTo( cartId );
      }
    }

    @AfterEach
    void tearDown() {
      api = null;
    }
  }

  @Nested
  @Description( "Given an existing shopping cart" )
  class GivenAnExistingShoppingCart {
    private ShoppingCartsApi api;
    private UUID             cartId;

    @BeforeEach
    void setUp() {
      ordersCheckoutPolicyService = mock( OrdersCheckoutPolicyService.class );
      api = new ShoppingCartsApiImpl( ordersCheckoutPolicyService,
                                      validationService,
                                      new ShoppingCartRepositoryInMemory() );
      cartId = api.createEmptyShoppingCart();
    }

    @Nested
    @Description( "when a valid item is added" )
    class WhenAValidItemIsAdded {

      private ShoppingCartItem item;

      @BeforeEach
      void setUp() {
        final Money singlePrice = Money.of( CurrencyUnit.of( "EUR" ), new BigDecimal( "1" ) );
        item = new ShoppingCartItem( ITEM_ID,
                                     "Whole Milk, Carton 0.5l",
                                     singlePrice );
        api.addItemToShoppingCart( cartId, item );
      }

      @Test
      void shouldContainTheItem() {
        assertThat( api.getShoppingCartItems( cartId ) ).isEqualTo( Collections.singletonList( item ) );
      }
    }

    @Nested
    @Description( "when an invalid item is added" )
    class WhenAnInvalidItemIsAdded {

      private ShoppingCartItem item;

      @BeforeEach
      void setUp() {
        doThrow(NoSuchElementException.class).when(validationService).validate( any(ShoppingCartItem.class) );
        final Money singlePrice = Money.of( CurrencyUnit.of( "EUR" ), new BigDecimal( "1" ) );
        item = new ShoppingCartItem( ITEM_ID,
                                     "Unknown product label",
                                     singlePrice );
      }

      @Test
      void shouldThrow() {
        assertThatThrownBy( () -> api.addItemToShoppingCart( cartId, item ) );
      }
    }

    @Nested
    @Description( "and it contains an item" )
    class AndAnItemExists {

      private ShoppingCartItem item;

      @BeforeEach
      void setUp() {
        final Money singlePrice = Money.of( CurrencyUnit.of( "EUR" ), new BigDecimal( "1" ) );
        item = new ShoppingCartItem( ITEM_ID,
                                     "Whole Milk, Carton 0.5l",
                                     singlePrice );
        api.addItemToShoppingCart( cartId, item );
      }

      @Nested
      @Description( "when the item is removed" )
      class WhenTheItemIsRemoved {

        @BeforeEach
        void setUp() {
          api.removeItemFromShoppingCart( cartId, item.getId() );
        }

        @Test
        void shouldBeEmpty() {
          assertThat( api.getShoppingCartItems( cartId ) ).isEqualTo( Collections.emptyList() );
        }
      }

      @Nested
      @Description( "when the cart is checked out" )
      class WhenTheCartIsCheckedOut {
        private UUID newCartId;

        @BeforeEach
        void setUp() {
          newCartId = api.checkOut( cartId );
        }

        @Test
        @Description( "the cart should no longer exist" )
        void cartShouldNoLongerExist() {
          assertThatThrownBy( () -> api.getShoppingCartById( cartId ) );
        }

        @Test
        @Description( "an order should be created" )
        void anOrderShouldBeCreated() {
          verify( ordersCheckoutPolicyService, times( 1 ) ).invoke( Collections.singletonList( item ) );
        }

        @Test
        @Description( "a new empty cart should be created" )
        void anEmptyCartShouldBeCreated() {
          assertThat( api.getShoppingCarts().get( 0 ).getId() ).isEqualTo( newCartId );
        }
      }
    }
  }
}
