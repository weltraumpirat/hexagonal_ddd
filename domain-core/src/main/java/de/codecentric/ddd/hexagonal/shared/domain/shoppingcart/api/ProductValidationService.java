package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api;

public interface ProductValidationService {
  void validate( ShoppingCartItem shoppingCartItem );
}
