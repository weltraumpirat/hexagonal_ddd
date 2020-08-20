package de.codecentric.ddd.hexagonal.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCart;
import de.codecentric.ddd.hexagonal.domain.shoppingcart.api.ShoppingCartItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShoppingCartItemsReadModel {
  private HashMap<UUID, List<ShoppingCartItem>> items;

  public ShoppingCartItemsReadModel() {
    this.items = new HashMap<>();
  }

  public List<ShoppingCartItem> read( final UUID cartId ) {
    return items.get( cartId ).stream().collect( Collectors.toUnmodifiableList() );
  }

  public void handleCartUpdated( final ShoppingCart cart ) {
    items.put( cart.getId(), cart.getItems() );
  }

  public void handleCartDeleted( final UUID cartId ) {
    items.remove( cartId );
  }

  public void handleCartCreated( final ShoppingCart cart ) {
    items.put( cart.getId(), Collections.emptyList() );

  }
}
