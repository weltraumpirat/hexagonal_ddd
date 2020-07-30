package de.codecentric.ddd.hexagonal.shared.config;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductsApi;
import de.codecentric.ddd.hexagonal.domain.product.impl.ProductsApiImpl;
import de.codecentric.ddd.hexagonal.shared.config.json.AmountModule;
import de.codecentric.ddd.hexagonal.shared.config.json.MoneyModule;
import de.codecentric.ddd.hexagonal.shared.config.json.PackagingTypeModule;
import de.codecentric.ddd.hexagonal.shared.product.persistence.ProductCrudRepository;
import de.codecentric.ddd.hexagonal.shared.product.persistence.ProductRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ExampleShopProductConfig {
  @Bean
  ProductsApi createProductApi( @Autowired final ProductCrudRepository jpaRepository ) {
    return new ProductsApiImpl( new ProductRepositoryJpa( jpaRepository ) );
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
