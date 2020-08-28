package de.codecentric.ddd.hexagonal.domain.product.api;

import de.codecentric.ddd.hexagonal.domain.product.impl.ProductListReadModel;
import de.codecentric.ddd.hexagonal.domain.product.impl.ProductShoppingListReadModel;
import de.codecentric.ddd.hexagonal.domain.product.impl.ProductValidationReadModel;
import de.codecentric.ddd.hexagonal.domain.product.impl.ProductsApiImpl;
import de.codecentric.ddd.hexagonal.product.persistence.ProductListRepositoryInMemory;
import de.codecentric.ddd.hexagonal.product.persistence.ProductRepositoryInMemory;
import de.codecentric.ddd.hexagonal.product.persistence.ProductShoppingListRepositoryInMemory;
import de.codecentric.ddd.hexagonal.product.persistence.ProductValidationRepositoryInMemory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

class ProductsApiTest {
  public static final UUID        UUID = java.util.UUID.randomUUID();
  private             ProductsApi api;

  @Nested
  @DisplayName( "Given an empty product list" )
  class GivenAnEmptyProductList {
    @BeforeEach
    void setUp() {
      api = new ProductsApiImpl( new ProductRepositoryInMemory(),
                                 new ProductValidationReadModel( new ProductValidationRepositoryInMemory() ),
                                 new ProductListReadModel( new ProductListRepositoryInMemory() ),
                                 new ProductShoppingListReadModel( new ProductShoppingListRepositoryInMemory() ) );
    }

    @Nested
    @DisplayName( "when a product is added" )
    class WhenAProductIsAdded {

      private Product product;

      @BeforeEach
      void setUp() {
        product =
          new Product( UUID,
                       "Whole Milk",
                       PackagingType.CARTON,
                       Money.of( CurrencyUnit.EUR, new BigDecimal( "1" ) ),
                       Fluid.HALF_LITRE );
        api.addProduct( product );
      }

      @Test
      @DisplayName( "should contain the product" )
      void shouldAddAProduct() {
        assertThat( api.getProducts() )
          .isEqualTo( Collections.singletonList( product ) );
        assertThat( api.getProductById( product.getId() ) ).isEqualTo( product );
      }
    }
  }

  @Nested
  @DisplayName( "Given one product in the list" )
  class GivenOneProductInTheList {

    @BeforeEach
    void setUp() {
      api = new ProductsApiImpl( new ProductRepositoryInMemory(),
                                 new ProductValidationReadModel( new ProductValidationRepositoryInMemory() ),
                                 new ProductListReadModel( new ProductListRepositoryInMemory() ),
                                 new ProductShoppingListReadModel( new ProductShoppingListRepositoryInMemory() ) );
      final Product product = new Product( UUID,
                                           "Whole Milk",
                                           PackagingType.CARTON,
                                           Money.of( CurrencyUnit.EUR, new BigDecimal( "1" ) ),
                                           Fluid.HALF_LITRE );
      api.addProduct( product );
    }

    @Nested
    @DisplayName( "when the product is removed" )
    class WhenTheProductIsRemoved {
      @BeforeEach
      void setUp() {
        api.removeProduct( UUID );

      }

      @Test
      @DisplayName( "should return an empty product list" )
      void shouldReturnEmptyList() {
        assertThat( api.getProducts() )
          .isEqualTo( Collections.emptyList() );
      }

      @Test
      @DisplayName( "should throw when the product is queried" )
      void shouldThrowWhenProductIsQueried() {
        assertThatThrownBy( () -> api.getProductById( UUID ) );
      }
    }
  }

  @AfterEach
  void tearDown() {
    api = null;
  }
}
