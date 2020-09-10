package de.codecentric.ddd.hexagonal.shared.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.ddd.hexagonal.domain.common.messaging.*;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartsApi;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartsCheckoutPolicyService;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.*;
import de.codecentric.ddd.hexagonal.shared.shoppingcart.impl.MessageLogger;
import de.codecentric.ddd.hexagonal.shared.shoppingcart.impl.OrdersCheckoutPolicyServiceRest;
import de.codecentric.ddd.hexagonal.shared.shoppingcart.impl.ProductValidationServiceRest;
import de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence.ShoppingCartItemsInfoRepositoryJpa;
import de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence.ShoppingCartListRowRepositoryJpa;
import de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence.ShoppingCartRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExampleShopShoppingCartConfig {
  @Bean
  RestTemplate restTemplate( RestTemplateBuilder builder ) {
    return builder.build();
  }

  @Bean( name = "eventbus" )
  Messagebus eventbus() {
    return new MessagebusLocal();
  }

  @Bean( name = "commandbus" )
  Messagebus commandbus() {
    return new MessagebusLocal();
  }

  @Bean
  TransactionFactory transactionFactory(
    @Autowired final Messagebus eventbus,
    @Autowired final Messagebus commandbus ) {
    return new TransactionFactory( eventbus, commandbus );
  }

  @Bean
  MessageLogger logger(
    @Autowired @Qualifier("customMapper") final ObjectMapper mapper,
    @Autowired final Messagebus eventbus,
    @Autowired final Messagebus commandbus ) {
    final MessageLogger logger = new MessageLogger( mapper );
    eventbus.register( Event.class, logger::onMessage );
    commandbus.register( Command.class, logger::onMessage );
    return logger;
  }

    @Bean
  ShoppingCartsCheckoutPolicyService shoppingCartsCheckoutPolicyService(
    @Autowired final TransactionFactory transactionFactory ) {
    return new ShoppingCartsCheckoutPolicyServiceInMemory( transactionFactory );
  }

  @Bean
  ShoppingCartFixture shoppingCartFixture(
    @Autowired final ShoppingCartRepositoryJpa repository,
    @Autowired final ProductValidationServiceRest productValidationService,
    @Autowired final OrdersCheckoutPolicyServiceRest ordersCheckoutPolicyService,
    @Autowired final ShoppingCartsCheckoutPolicyService shoppingCartsCheckoutPolicyService,
    @Autowired final Messagebus eventbus,
    @Autowired final Messagebus commandbus ) {
    return new ShoppingCartFixture(
      repository,
      productValidationService,
      ordersCheckoutPolicyService,
      shoppingCartsCheckoutPolicyService,
      eventbus, commandbus
    );
  }

  @Bean
  ShoppingCartListReadModel shoppingCartListReadModel(
    @Autowired final ShoppingCartListRowRepositoryJpa repository,
    @Autowired final Messagebus eventbus ) {
    return new ShoppingCartListReadModel( repository, eventbus );
  }

  @Bean
  ShoppingCartItemsReadModel shoppingCartItemsReadModel(
    @Autowired final ShoppingCartItemsInfoRepositoryJpa repository,
    @Autowired final Messagebus eventbus ) {
    return new ShoppingCartItemsReadModel( repository, eventbus );
  }

  @Bean
  ShoppingCartsApi createShoppingCartsApi(
    @Autowired final ShoppingCartFixture shoppingCartFixture,
    @Autowired final ShoppingCartItemsReadModel itemsReadModel,
    @Autowired final ShoppingCartListReadModel listReadModel,
    @Autowired final TransactionFactory transactionFactory ) {
    return new ShoppingCartsApiImpl( shoppingCartFixture,
                                     listReadModel,
                                     itemsReadModel,
                                     transactionFactory );
  }
}
