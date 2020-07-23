package de.codecentric.ddd.hexagonal.monolith.domain.product.api;

import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.monolith.domain.order.impl.OrdersApiImpl;
import static de.codecentric.ddd.hexagonal.monolith.domain.product.api.Fluid.HALF_LITRE;
import de.codecentric.ddd.hexagonal.monolith.domain.product.impl.ProductsApiImpl;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartItem;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartsApi;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl.CheckoutService;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl.ProductValidationService;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl.ShoppingCartsApiImpl;
import de.codecentric.ddd.hexagonal.monolith.persistence.OrderRepositoryInMemory;
import de.codecentric.ddd.hexagonal.monolith.persistence.ProductRepositoryInMemory;
import de.codecentric.ddd.hexagonal.monolith.persistence.ShoppingCartRepositoryInMemory;
import jdk.jfr.Description;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

public class ShoppingCartsApiTest {

  public static final UUID            ITEM_ID = UUID.randomUUID();
  private             OrdersApi       ordersApi;
  private             CheckoutService checkoutService;
  private ProductsApiImpl productsApi;
  @BeforeEach
   void setUp() {
    productsApi = new ProductsApiImpl( new ProductRepositoryInMemory() );
    productsApi.addProduct( new Product( UUID.randomUUID(), "Whole Milk", PackagingType.CARTON, Money.of( CurrencyUnit.EUR, new BigDecimal( "1" ) ), HALF_LITRE ) );
  }

  @Nested
  @Description( "Given no prior shoppingcarts" )
  class GivenNoShoppingCarts {
    private ShoppingCartsApi api;

    @BeforeEach
    void setUp() {
      ordersApi = new OrdersApiImpl( new OrderRepositoryInMemory() );
      checkoutService = new CheckoutService( ordersApi );
      this.api = new ShoppingCartsApiImpl( checkoutService, new ProductValidationService(
        productsApi ), new ShoppingCartRepositoryInMemory() );
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
        assertThat( api.getShoppingCarts().get( 0 ).getId() ).isEqualTo( cartId );
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
      ordersApi = new OrdersApiImpl( new OrderRepositoryInMemory() );
      checkoutService = new CheckoutService( ordersApi );
      api = new ShoppingCartsApiImpl( checkoutService, new ProductValidationService( productsApi ), new ShoppingCartRepositoryInMemory() );
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
        final Money singlePrice = Money.of( CurrencyUnit.of( "EUR" ), new BigDecimal( "1" ) );
        item = new ShoppingCartItem( ITEM_ID,
                                     "Unknown product label",
                                     singlePrice );
      }

      @Test
      void shouldThrow() {
        assertThatThrownBy(()-> api.addItemToShoppingCart( cartId, item ));
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

        @BeforeEach
        void setUp() {
          api.checkOut( cartId );
        }

        @Test
        void cartShouldNoLongerExist() {
          assertThat( api.getShoppingCarts() ).isEqualTo( Collections.emptyList() );
        }

        @Test
        void anOrderShouldBeCreated() {
          assertThat( ordersApi.getOrders() ).isNotEqualTo( Collections.emptyList() );
        }
      }
    }
  }
}
