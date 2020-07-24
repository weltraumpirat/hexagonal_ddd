package de.codecentric.ddd.hexagonal.monolith.domain.shoppingcart.api;

import java.util.UUID;

public interface ShoppingCartRepository {

  void create( final ShoppingCart cart );

  ShoppingCart findById( final UUID cartId );

  void update( final ShoppingCart shoppingCart );

  void delete( final UUID cartId );
}
