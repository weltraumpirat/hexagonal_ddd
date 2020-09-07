package de.codecentric.ddd.hexagonal.shared.config;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.codecentric.ddd.hexagonal.domain.common.messaging.MessagebusLocal;
import de.codecentric.ddd.hexagonal.domain.common.messaging.TransactionFactory;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductsApi;
import de.codecentric.ddd.hexagonal.domain.product.impl.*;
import de.codecentric.ddd.hexagonal.shared.config.json.AmountModule;
import de.codecentric.ddd.hexagonal.shared.config.json.MoneyModule;
import de.codecentric.ddd.hexagonal.shared.config.json.PackagingTypeModule;
import de.codecentric.ddd.hexagonal.shared.product.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ExampleShopProductConfig {
  @Bean
  ProductsApi createProductApi( @Autowired final ProductCrudRepository crudRepository,
                                @Autowired final ProductValidationCrudRepository validationCrudRepository,
                                @Autowired final ProductListCrudRepository listCrudRepository,
                                @Autowired final ProductShoppingListCrudRepository shoppingListCrudRepository ) {
    final MessagebusLocal commandbus = new MessagebusLocal();
    final MessagebusLocal eventbus = new MessagebusLocal();
    final TransactionFactory transactionFactory = new TransactionFactory( eventbus, commandbus );
    final ProductsFixture fixture =
      new ProductsFixture( new ProductRepositoryJpa( crudRepository ), eventbus, commandbus );
    final ProductValidationReadModel validationReadModel =
      new ProductValidationReadModel( new ProductValidationRepositoryJpa( validationCrudRepository ), eventbus );
    final ProductListReadModel productListReadModel =
      new ProductListReadModel( new ProductListRepositoryJpa( listCrudRepository ), eventbus );
    final ProductShoppingListReadModel shoppingListReadModel =
      new ProductShoppingListReadModel( new ProductShoppingListRepositoryJpa( shoppingListCrudRepository ), eventbus );

    return new ProductsApiImpl( fixture,
                                validationReadModel,
                                productListReadModel,
                                shoppingListReadModel,
                                transactionFactory );
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
