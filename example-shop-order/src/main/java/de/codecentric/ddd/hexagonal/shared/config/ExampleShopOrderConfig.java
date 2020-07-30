package de.codecentric.ddd.hexagonal.shared.config;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.codecentric.ddd.hexagonal.shared.config.json.AmountModule;
import de.codecentric.ddd.hexagonal.shared.config.json.MoneyModule;
import de.codecentric.ddd.hexagonal.shared.config.json.PackagingTypeModule;
import de.codecentric.ddd.hexagonal.shared.domain.order.api.OrdersApi;
import de.codecentric.ddd.hexagonal.shared.domain.order.impl.OrdersApiImpl;
import de.codecentric.ddd.hexagonal.shared.order.persistence.OrderCrudRepository;
import de.codecentric.ddd.hexagonal.shared.order.persistence.OrderPositionCrudRepository;
import de.codecentric.ddd.hexagonal.shared.order.persistence.OrderRepositoryJpa;
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
                             @Autowired final OrderPositionCrudRepository positionsRepository ) {
    return new OrdersApiImpl( new OrderRepositoryJpa( orderRepository, positionsRepository ) );
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
