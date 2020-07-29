package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartRepository {

  void create( final ShoppingCart cart );

  List<ShoppingCart> findAll();
  ShoppingCart findById( final UUID cartId );

  void update( final ShoppingCart shoppingCart );

  void delete( final UUID cartId );
}
