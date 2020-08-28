package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.UUID;

public interface ProductValidationRepository {
  ProductValidationEntry findByLabel( String label );

  void create( ProductValidationEntry product );

  void delete( UUID id );
}
