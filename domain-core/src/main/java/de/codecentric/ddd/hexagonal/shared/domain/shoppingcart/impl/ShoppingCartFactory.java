package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCart;

public class ShoppingCartFactory {
  public static ShoppingCartEntity create( final ShoppingCart cart ) {
    return new ShoppingCartEntity( cart.getId(), cart.getItems() );
  }

  public static ShoppingCart create( final ShoppingCartEntity cart ) {
    return new ShoppingCart( cart.getId(), cart.getItems() );
  }
}
