package de.codecentric.ddd.hexagonal.monolith.product.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductCrudRepository extends CrudRepository<ProductEntity, UUID> {
}
