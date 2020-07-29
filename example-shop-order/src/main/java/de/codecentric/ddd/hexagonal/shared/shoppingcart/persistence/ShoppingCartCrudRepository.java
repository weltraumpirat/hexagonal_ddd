package de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ShoppingCartCrudRepository extends CrudRepository<ShoppingCartEntity, UUID> {
}
