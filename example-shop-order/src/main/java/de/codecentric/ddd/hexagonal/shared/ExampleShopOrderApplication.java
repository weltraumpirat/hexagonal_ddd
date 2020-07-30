package de.codecentric.ddd.hexagonal.shared;

import de.codecentric.ddd.hexagonal.shared.config.ExampleShopOrderConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ ExampleShopOrderConfig.class })

public class ExampleShopOrderApplication {

  public static void main( String[] args ) {
    SpringApplication.run( ExampleShopOrderApplication.class, args );
  }
}
