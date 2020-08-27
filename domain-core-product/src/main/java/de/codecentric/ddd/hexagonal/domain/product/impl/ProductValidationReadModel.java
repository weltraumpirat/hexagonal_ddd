package de.codecentric.ddd.hexagonal.domain.product.impl;

import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductValidationRepository;

import java.util.UUID;

public class ProductValidationReadModel {
  private final ProductValidationRepository validationRepository;

  public ProductValidationReadModel(
    final ProductValidationRepository validationRepository ) {
    this.validationRepository = validationRepository;
  }

  public Product read( final String label ) {
    return validationRepository.findByLabel( label );
  }

  public void onProductCreated( final Product product ) {
    validationRepository.create( product );
  }

  public void onProductRemoved( final UUID productId ) {
    validationRepository.delete( productId );
  }
}
