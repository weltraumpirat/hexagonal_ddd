package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.UUID;

public interface ProductValidationRepository {
  Product findByLabel( String label );

  void create( Product product );

  void delete( UUID id );
}
