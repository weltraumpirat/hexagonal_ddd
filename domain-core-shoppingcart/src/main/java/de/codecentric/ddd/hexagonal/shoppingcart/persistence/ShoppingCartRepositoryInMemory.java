package de.codecentric.ddd.hexagonal.shoppingcart.persistence;


import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartNotFoundException;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShoppingCartRepositoryInMemory implements ShoppingCartRepository {
  private final Map<UUID, ShoppingCart> carts = new HashMap<>();


  @Override public void create( final ShoppingCart cart ) {
    carts.put( cart.getId(), cart );
  }

  @Override public List<ShoppingCart> findAll( ) {
    return carts.values().stream()
             .collect( Collectors.toUnmodifiableList() );
  }

  @Override public ShoppingCart findById( final UUID cartId ) {
    final ShoppingCart cart = carts.get( cartId );
    if(cart == null) throw new ShoppingCartNotFoundException();
    return cart;
  }

  @Override public void update( final ShoppingCart shoppingCart ) {
    carts.put( shoppingCart.getId(), shoppingCart );
  }

  @Override public void delete( final UUID cartId ) {
    carts.remove( cartId );
  }
}
