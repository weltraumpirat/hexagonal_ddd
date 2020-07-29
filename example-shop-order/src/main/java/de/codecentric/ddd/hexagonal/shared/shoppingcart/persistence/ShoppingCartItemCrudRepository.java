package de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ShoppingCartItemCrudRepository extends CrudRepository<ShoppingCartItemEntity, UUID> {
  Iterable<ShoppingCartItemEntity> findByCartId(final UUID cartId);
  void deleteAllByCartId( final UUID cartId );
}
