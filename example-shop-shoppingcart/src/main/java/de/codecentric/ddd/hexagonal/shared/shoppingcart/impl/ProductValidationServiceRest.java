package de.codecentric.ddd.hexagonal.shared.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.shared.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ProductValidationService;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCartItem;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

public class ProductValidationServiceRest implements ProductValidationService {
  private final RestTemplate template;

  public ProductValidationServiceRest( final RestTemplate template ) {
    this.template = template;
  }

  @Override public void validate( final ShoppingCartItem shoppingCartItem ) {
    final Product[] result = Optional.ofNullable( template.getForObject( "http://example-shop-product:8080/api/product", Product[].class )
                                                ).orElse( new Product[]{} );
    Arrays.stream( result )
      .filter( p -> shoppingCartItem.getLabel().equals( p.toLabel() ) )
      .findAny()
      .orElseThrow();
  }
}
