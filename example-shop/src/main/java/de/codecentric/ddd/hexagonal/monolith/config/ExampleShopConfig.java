package de.codecentric.ddd.hexagonal.monolith.config;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.codecentric.ddd.hexagonal.monolith.config.json.AmountModule;
import de.codecentric.ddd.hexagonal.monolith.config.json.MoneyModule;
import de.codecentric.ddd.hexagonal.monolith.config.json.PackagingTypeModule;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.monolith.domain.order.impl.OrdersApiImpl;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.ProductsApi;
import de.codecentric.ddd.hexagonal.monolith.domain.product.impl.ProductsApiImpl;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartsApi;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl.CheckoutService;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl.ProductValidationService;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl.ShoppingCartsApiImpl;
import de.codecentric.ddd.hexagonal.monolith.persistence.OrderRepositoryInMemory;
import de.codecentric.ddd.hexagonal.monolith.persistence.ProductRepositoryInMemory;
import de.codecentric.ddd.hexagonal.monolith.persistence.ShoppingCartRepositoryInMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ExampleShopConfig {
  @Bean
   ProductsApi createProductApi() {
    return new ProductsApiImpl( new ProductRepositoryInMemory() );
  }

  @Bean
   OrdersApi createOrdersApi() {
    return new OrdersApiImpl( new OrderRepositoryInMemory() );
  }

  @Bean
   ShoppingCartsApi createShoppingCartsApi(@Autowired final OrdersApi ordersApi, @Autowired final ProductsApi productsApi ) {
    return new ShoppingCartsApiImpl( new CheckoutService( ordersApi ), new ProductValidationService( productsApi ), new ShoppingCartRepositoryInMemory() );
  }

  @Bean
  @Primary
  public static ObjectMapper createObjectMapper() {
    ObjectMapper objectmapper = new ObjectMapper();
    objectmapper.setVisibility( FIELD, ANY );
    objectmapper.registerModule( new ParameterNamesModule() );
    objectmapper.registerModule( new PackagingTypeModule() );
    objectmapper.registerModule( new AmountModule() );
    objectmapper.registerModule( new MoneyModule() );
    return objectmapper;
  }
}
