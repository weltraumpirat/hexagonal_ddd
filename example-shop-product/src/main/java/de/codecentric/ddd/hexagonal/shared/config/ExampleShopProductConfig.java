package de.codecentric.ddd.hexagonal.shared.config;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.codecentric.ddd.hexagonal.domain.common.messaging.*;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductsApi;
import de.codecentric.ddd.hexagonal.domain.product.impl.*;
import de.codecentric.ddd.hexagonal.shared.config.json.AmountModule;
import de.codecentric.ddd.hexagonal.shared.config.json.MoneyModule;
import de.codecentric.ddd.hexagonal.shared.config.json.PackagingTypeModule;
import de.codecentric.ddd.hexagonal.shared.product.MessageLogger;
import de.codecentric.ddd.hexagonal.shared.product.persistence.ProductListRepositoryJpa;
import de.codecentric.ddd.hexagonal.shared.product.persistence.ProductRepositoryJpa;
import de.codecentric.ddd.hexagonal.shared.product.persistence.ProductShoppingListRepositoryJpa;
import de.codecentric.ddd.hexagonal.shared.product.persistence.ProductValidationRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ExampleShopProductConfig {
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
  public ProductsFixture productsFixture(
    @Autowired final ProductRepositoryJpa repository,
    @Autowired final Messagebus eventbus,
    @Autowired final Messagebus commandbus ) {
    return new ProductsFixture( repository, eventbus, commandbus );
  }

  @Bean
  public ProductValidationReadModel productValidationReadModel(
    @Autowired final ProductValidationRepositoryJpa validationRepository,
    @Autowired final Messagebus eventbus
                                                              ) {
    return new ProductValidationReadModel( validationRepository, eventbus );
  }

  @Bean
  public ProductListReadModel productListReadModel(
    @Autowired final ProductListRepositoryJpa repository,
    @Autowired final Messagebus eventbus ) {
    return new ProductListReadModel( repository, eventbus );
  }

  @Bean
  public ProductShoppingListReadModel productShoppingListReadModel(
    @Autowired final ProductShoppingListRepositoryJpa repository,
    @Autowired final Messagebus eventbus ) {
    return new ProductShoppingListReadModel( repository, eventbus );
  }

  @Bean
  ProductsApi createProductApi(
    @Autowired final ProductsFixture productsFixture,
    @Autowired final TransactionFactory transactionFactory,
    @Autowired final ProductValidationReadModel validationReadModel,
    @Autowired final ProductListReadModel productListReadModel,
    @Autowired final ProductShoppingListReadModel shoppingListReadModel ) {
    return new ProductsApiImpl( productsFixture,
                                validationReadModel,
                                productListReadModel,
                                shoppingListReadModel,
                                transactionFactory );
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
