package de.codecentric.ddd.hexagonal.shared;

import de.codecentric.ddd.hexagonal.shared.config.ExampleShopShoppingCartConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ ExampleShopShoppingCartConfig.class })

public class ExampleShopShoppingCartApplication {

  public static void main( String[] args ) {
    SpringApplication.run( ExampleShopShoppingCartApplication.class, args );
  }
}
