package de.codecentric.ddd.hexagonal.domain.product.api;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.common.messaging.MessagebusLocal;
import de.codecentric.ddd.hexagonal.domain.common.messaging.TransactionFactory;
import de.codecentric.ddd.hexagonal.domain.product.impl.*;
import de.codecentric.ddd.hexagonal.product.persistence.ProductListRepositoryInMemory;
import de.codecentric.ddd.hexagonal.product.persistence.ProductRepositoryInMemory;
import de.codecentric.ddd.hexagonal.product.persistence.ProductShoppingListRepositoryInMemory;
import de.codecentric.ddd.hexagonal.product.persistence.ProductValidationRepositoryInMemory;
import lombok.extern.java.Log;
import static org.assertj.core.api.Assertions.assertThat;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Log
class ProductsApiTest {
  public static final UUID        UUID = java.util.UUID.randomUUID();
  private             ProductsApi api;
  private static Messagebus eventbus;
  private static Messagebus commandbus;
  private static TransactionFactory transactionFactory;

  @BeforeAll
  static void setUpClass() {
    eventbus = new MessagebusLocal();
    commandbus = new MessagebusLocal();
    transactionFactory = new TransactionFactory( eventbus, commandbus );

  }

  @Nested
  @DisplayName( "Given an empty product list" )
  class GivenAnEmptyProductList {
    @BeforeEach
    void setUp() {
      api = createProductsApi();
    }

    @Nested
    @DisplayName( "when a product is added" )
    class WhenAProductIsAdded {

      private Product product;

      @BeforeEach
      void setUp() throws ExecutionException, InterruptedException {
        product =
          new Product( UUID,
                       "Whole Milk",
                       PackagingType.CARTON,
                       Money.of( CurrencyUnit.EUR, new BigDecimal( "1" ) ),
                       Fluid.HALF_LITRE );
        api.addProduct( product ).get();
      }

      @Test
      @DisplayName( "should contain the product" )
      void shouldAddAProduct() {
        assertThat( api.getProductList().get( 0 ).getId() ).isEqualTo( product.getId() );
      }
    }
  }

  private ProductsApi createProductsApi() {
    final ProductsFixture productsFixture =
      new ProductsFixture( new ProductRepositoryInMemory(), eventbus, commandbus );
    final ProductValidationReadModel validationReadModel =
      new ProductValidationReadModel( new ProductValidationRepositoryInMemory(), eventbus );
    final ProductListReadModel listReadModel =
      new ProductListReadModel( new ProductListRepositoryInMemory(), eventbus );
    final ProductShoppingListReadModel shoppingListReadModel =
      new ProductShoppingListReadModel( new ProductShoppingListRepositoryInMemory(),
                                        eventbus );
    return new ProductsApiImpl( productsFixture,
                               validationReadModel,
                               listReadModel,
                               shoppingListReadModel,
                               transactionFactory );
  }

  @Nested
  @DisplayName( "Given one product in the list" )
  class GivenOneProductInTheList {

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
      api = createProductsApi();
      final Product product = new Product( UUID,
                                           "Whole Milk",
                                           PackagingType.CARTON,
                                           Money.of( CurrencyUnit.EUR, new BigDecimal( "1" ) ),
                                           Fluid.HALF_LITRE );
      api.addProduct( product ).get();
    }

    @Nested
    @DisplayName( "when the product is removed" )
    class WhenTheProductIsRemoved {
      @BeforeEach
      void setUp() throws ExecutionException, InterruptedException {
        api.removeProduct( UUID ).get();
      }

      @Test
      @DisplayName( "should return an empty product list" )
      void shouldReturnEmptyList() {
        assertThat( api.getProductList() )
          .isEqualTo( Collections.emptyList() );
      }
    }
  }

  @AfterEach
  void tearDown() {
    eventbus.unregisterAll(  );
    commandbus.unregisterAll(  );
    api = null;
  }
}
