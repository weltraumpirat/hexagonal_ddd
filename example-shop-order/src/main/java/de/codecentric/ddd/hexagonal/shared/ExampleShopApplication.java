package de.codecentric.ddd.hexagonal.shared;

import de.codecentric.ddd.hexagonal.shared.config.ExampleShopConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ ExampleShopConfig.class })

public class ExampleShopApplication {

  public static void main( String[] args ) {
    SpringApplication.run( ExampleShopApplication.class, args );
  }
}
