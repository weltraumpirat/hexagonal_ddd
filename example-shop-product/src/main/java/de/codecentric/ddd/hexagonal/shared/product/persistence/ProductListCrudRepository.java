package de.codecentric.ddd.hexagonal.shared.product.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductListCrudRepository extends CrudRepository<ProductListRowEntity, UUID> {
}
