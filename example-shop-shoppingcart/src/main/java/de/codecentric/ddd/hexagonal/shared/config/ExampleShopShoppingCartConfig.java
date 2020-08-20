package de.codecentric.ddd.hexagonal.shared.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartsApi;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartItemsReadModel;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartListReadModel;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartsApiImpl;
import de.codecentric.ddd.hexagonal.shared.shoppingcart.impl.OrdersCheckoutPolicyServiceRest;
import de.codecentric.ddd.hexagonal.shared.shoppingcart.impl.ProductValidationServiceRest;
import de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExampleShopShoppingCartConfig {
  @Bean
  public RestTemplate restTemplate( RestTemplateBuilder builder ) {
    return builder.build();
  }

  @Bean
  ShoppingCartsApi createShoppingCartsApi( @Autowired final RestTemplate restTemplate,
                                           @Autowired final ShoppingCartCrudRepository cartRepository,
                                           @Autowired final ShoppingCartItemCrudRepository cartItemRepository,
                                           @Autowired final ShoppingCartItemInfoCrudRepository cartItemInfoRepository,
                                           @Autowired final ShoppingCartListRowCrudRepository listRowRepository,
                                           @Autowired final ObjectMapper objectMapper) {
    return new ShoppingCartsApiImpl( new OrdersCheckoutPolicyServiceRest( restTemplate ),
                                     new ProductValidationServiceRest( restTemplate ),
                                     new ShoppingCartRepositoryJpa( cartRepository, cartItemRepository ),
                                     new ShoppingCartListReadModel( new ShoppingCartListRowRepositoryJpa( listRowRepository )),
                                     new ShoppingCartItemsReadModel( new ShoppingCartItemsInfoRepositoryJpa( cartItemInfoRepository, objectMapper)) );
  }
}
