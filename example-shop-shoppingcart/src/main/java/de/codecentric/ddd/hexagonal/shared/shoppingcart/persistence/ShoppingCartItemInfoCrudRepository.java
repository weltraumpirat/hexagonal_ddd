package de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartItemsInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ShoppingCartItemInfoCrudRepository extends CrudRepository<ShoppingCartItemsInfoEntity, UUID> {
}
