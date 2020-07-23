package de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.monolith.domain.product.api.ProductsApi;
import de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api.ShoppingCartItem;

public class ProductValidationService {
  private final ProductsApi api;

  public ProductValidationService( final ProductsApi api ) {
    this.api = api;
  }

  public void validate( final ShoppingCartItem shoppingCartItem ) {
    api.getProducts().stream()
      .filter( p -> shoppingCartItem.getLabel().equals( p.toLabel() ) )
      .findAny()
      .orElseThrow();
  }
}
