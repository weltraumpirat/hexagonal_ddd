package de.codecentric.ddd.hexagonal.shared.config;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.codecentric.ddd.hexagonal.domain.common.messaging.MessagebusLocal;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.domain.order.impl.CreateOrderHandler;
import de.codecentric.ddd.hexagonal.domain.order.impl.OrdersApiImpl;
import de.codecentric.ddd.hexagonal.domain.order.impl.OrdersListReadModel;
import de.codecentric.ddd.hexagonal.shared.config.json.AmountModule;
import de.codecentric.ddd.hexagonal.shared.config.json.MoneyModule;
import de.codecentric.ddd.hexagonal.shared.config.json.PackagingTypeModule;
import de.codecentric.ddd.hexagonal.shared.order.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Bean
  OrdersApi createOrdersApi( @Autowired final OrderCrudRepository orderRepository,
                             @Autowired final OrderPositionCrudRepository positionsRepository,
                             @Autowired final OrdersListCrudRepository ordersListRowCrudRepository,
                             @Autowired final OrdersListPositionsCrudRepository ordersListPositionsCrudRepository ) {
    final MessagebusLocal commandbus = new MessagebusLocal();
    final MessagebusLocal eventbus = new MessagebusLocal();
    final CreateOrderHandler createOrderHandler =
      new CreateOrderHandler( new OrderRepositoryJpa( orderRepository, positionsRepository ), eventbus );

    final OrdersListReadModel ordersListReadModel = new OrdersListReadModel(
      new OrdersListRepositoryJpa( ordersListRowCrudRepository, ordersListPositionsCrudRepository ) );

    return new OrdersApiImpl(
      ordersListReadModel,
      eventbus,
      commandbus,
      createOrderHandler );
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
