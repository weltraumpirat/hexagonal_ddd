package de.codecentric.ddd.hexagonal.shared;

import de.codecentric.ddd.hexagonal.shared.config.ExampleShopShoppingCartConfig;
import de.codecentric.ddd.hexagonal.shared.shoppingcart.impl.MessageLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ ExampleShopShoppingCartConfig.class })
public class ExampleShopShoppingCartApplication {
  @Autowired MessageLogger logger;
  public static void main( String[] args ) {
    SpringApplication.run( ExampleShopShoppingCartApplication.class, args );
  }
}
