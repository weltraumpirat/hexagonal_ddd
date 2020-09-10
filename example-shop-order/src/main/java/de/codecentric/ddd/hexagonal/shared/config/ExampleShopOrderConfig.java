package de.codecentric.ddd.hexagonal.shared.config;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.codecentric.ddd.hexagonal.domain.common.messaging.*;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.domain.order.impl.OrdersApiImpl;
import de.codecentric.ddd.hexagonal.domain.order.impl.OrdersFixture;
import de.codecentric.ddd.hexagonal.domain.order.impl.OrdersListReadModel;
import de.codecentric.ddd.hexagonal.shared.config.json.AmountModule;
import de.codecentric.ddd.hexagonal.shared.config.json.MoneyModule;
import de.codecentric.ddd.hexagonal.shared.config.json.PackagingTypeModule;
import de.codecentric.ddd.hexagonal.shared.order.MessageLogger;
import de.codecentric.ddd.hexagonal.shared.order.persistence.OrderRepositoryJpa;
import de.codecentric.ddd.hexagonal.shared.order.persistence.OrdersListRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExampleShopOrderConfig {
  @Bean
  public RestTemplate restTemplate( RestTemplateBuilder builder ) {
    return builder.build();
  }

  @Bean( name = "eventbus" )
  public Messagebus eventbus() {
    return new MessagebusLocal();
  }

  @Bean( name = "commandbus" )
  public Messagebus commandbus() {
    return new MessagebusLocal();
  }

  @Bean
  public TransactionFactory transactionFactory(
    @Autowired final Messagebus eventbus, final Messagebus commandbus ) {
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
  public OrdersListReadModel ordersListReadModel(
    @Autowired final OrdersListRepositoryJpa repository,
    @Autowired final Messagebus eventbus ) {
    return new OrdersListReadModel( repository, eventbus );
  }

  @Bean
  public OrdersFixture ordersFixture(
    @Autowired final OrderRepositoryJpa orderRepository,
    @Autowired final Messagebus eventbus,
    @Autowired final Messagebus commandbus ) {
    return new OrdersFixture( orderRepository, eventbus, commandbus );

  }

  @Bean
  OrdersApi createOrdersApi(
    @Autowired final OrdersListReadModel ordersListReadModel,
    @Autowired final OrdersFixture ordersFixture,
    @Autowired TransactionFactory transactionFactory ) {
    return new OrdersApiImpl( ordersFixture, ordersListReadModel, transactionFactory );
  }

  @Bean
  @Primary
  @Qualifier("customMapper")
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
