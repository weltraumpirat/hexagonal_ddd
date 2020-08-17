package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;

import java.util.ArrayList;
import java.util.UUID;

public class ShoppingCartFactory {
  public static ShoppingCartEntity create( final ShoppingCart cart ) {
    return new ShoppingCartEntity( cart.getId(), cart.getItems() );
  }

  public static ShoppingCart create( final ShoppingCartEntity cart ) {
    return new ShoppingCart( cart.getId(), cart.getItems() );
  }

  public static ShoppingCart create( ) {
    return new ShoppingCart( UUID.randomUUID(), new ArrayList<>() );
  }
}
