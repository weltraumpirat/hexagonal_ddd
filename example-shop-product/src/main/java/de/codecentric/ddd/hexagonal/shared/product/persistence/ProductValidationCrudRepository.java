package de.codecentric.ddd.hexagonal.shared.product.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductValidationCrudRepository extends CrudRepository<ProductValidationEntity, UUID> {
  @Query ProductValidationEntity findByLabel( String label );
}
