package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShoppingCartListReadModel {
  private final HashMap<UUID, ShoppingCart> shoppingCarts;

  public ShoppingCartListReadModel() {
    shoppingCarts = new HashMap<>();
  }

  public List<ShoppingCart> read() {
    return shoppingCarts.values().stream().collect( Collectors.toUnmodifiableList() );
  }

  public void handleCartCreated( ShoppingCart cart ) {
    shoppingCarts.put( cart.getId(), cart );
  }

  public void handleCartDeleted( UUID id ) {
    shoppingCarts.remove( id );
  }

  public void handleCartUpdated( final ShoppingCart cart ) {
    shoppingCarts.put( cart.getId(), cart );
  }
}
