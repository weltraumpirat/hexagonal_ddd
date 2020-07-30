package de.codecentric.ddd.hexagonal.domain.shoppingcart.api;

public interface ProductValidationService {
  void validate( ShoppingCartItem shoppingCartItem );
}
