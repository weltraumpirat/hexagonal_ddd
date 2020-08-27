package de.codecentric.ddd.hexagonal.shared.product.persistence;

import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductValidationRepository;

import java.util.UUID;

public class ProductValidationRepositoryJpa implements ProductValidationRepository {
  @Override public void create( final Product product ) {

  }

  @Override public void delete( final UUID id ) {

  }

  @Override public Product findByLabel( final String label ) {
    return null;
  }
}
