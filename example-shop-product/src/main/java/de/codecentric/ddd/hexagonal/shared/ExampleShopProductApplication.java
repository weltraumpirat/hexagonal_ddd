package de.codecentric.ddd.hexagonal.shared;

import de.codecentric.ddd.hexagonal.shared.config.ExampleShopProductConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ ExampleShopProductConfig.class })

public class ExampleShopProductApplication {

  public static void main( String[] args ) {
    SpringApplication.run( ExampleShopProductApplication.class, args );
  }
}
