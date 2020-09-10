package de.codecentric.ddd.hexagonal.shared;

import de.codecentric.ddd.hexagonal.shared.config.ExampleShopProductConfig;
import de.codecentric.ddd.hexagonal.shared.product.MessageLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import( { ExampleShopProductConfig.class } )

public class ExampleShopProductApplication {

  @Autowired MessageLogger logger;

  public static void main( String[] args ) {
    SpringApplication.run( ExampleShopProductApplication.class, args );
  }
}
