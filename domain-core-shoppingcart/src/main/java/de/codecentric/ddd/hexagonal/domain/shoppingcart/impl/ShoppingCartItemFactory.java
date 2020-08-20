package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;

import java.util.UUID;

class ShoppingCartItemFactory {
  public static ShoppingCartItem create( final ShoppingCartItem item ) {
    final UUID uuid = item.getId() != null ? item.getId() : UUID.randomUUID();
    return new ShoppingCartItem( uuid, item.getLabel(), item.getPrice() );
  }
}
