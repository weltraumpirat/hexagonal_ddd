package de.codecentric.ddd.hexagonal.shared.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ProductValidationService;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ProductValidationServiceRest implements ProductValidationService {
  private final RestTemplate template;

  public ProductValidationServiceRest( final RestTemplate template ) {
    this.template = template;
  }

  @Override public void validate( final ShoppingCartItem shoppingCartItem ) {
    final Map<String, String> variables = Map.of( "label", shoppingCartItem.getLabel() );
    final ResponseEntity<Void> response = template.getForEntity(
      "http://example-shop-product:8080/api/product",
      Void.class,
      variables );
    if( !response.getStatusCode().is2xxSuccessful() ) {
      throw new NoSuchElementException();
    }
  }
}
