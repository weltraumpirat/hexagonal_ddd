package de.codecentric.ddd.hexagonal.domain.product.api;

import de.codecentric.ddd.hexagonal.domain.product.impl.ProductsApiImpl;
import de.codecentric.ddd.hexagonal.product.persistence.ProductRepositoryInMemory;
import jdk.jfr.Description;
import org.assertj.core.api.Assertions;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

class ProductsApiTest {
  public static final UUID        UUID = java.util.UUID.randomUUID();
  private             ProductsApi api;

  @Nested
  @Description( "Given an empty product list" )
  class GivenAnEmptyProductList {
    @BeforeEach
    void setUp() {
      api = new ProductsApiImpl( new ProductRepositoryInMemory() );
    }

    @Nested
    @Description( "when a product is added" )
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
      @Description( "should contain a product" )
      void shouldAddAProduct() {
        Assertions.assertThat( api.getProducts() )
          .isEqualTo( Collections.singletonList( product ) );
      }
    }
  }

  @Nested
  @Description( "Given one product in the list" )
  class GivenOneProductInTheList {

    @BeforeEach
    void setUp() {
      api = new ProductsApiImpl( new ProductRepositoryInMemory() );
      final Product product = new Product( UUID,
                                           "Whole Milk",
                                           PackagingType.CARTON,
                                           Money.of( CurrencyUnit.EUR, new BigDecimal( "1" ) ),
                                           Fluid.HALF_LITRE );
      api.addProduct( product );
    }

    @Nested
    @Description( "when the product is removed" )
    class WhenTheProductIsRemoved {
      @BeforeEach
      void setUp() {
        api.removeProduct( UUID );

      }

      @Test
      @Description( "should return an empty product list" )
      void shouldReturnEmptyList() {
        Assertions.assertThat( api.getProducts() )
          .isEqualTo( Collections.emptyList() );
      }
    }
  }

  @AfterEach
  void tearDown() {
    api = null;
  }
}
