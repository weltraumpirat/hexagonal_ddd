package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.shared.domain.product.api.ProductsApi;
import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCartItem;
import lombok.extern.java.Log;

@Log
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
