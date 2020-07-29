package de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.impl;

import de.codecentric.ddd.hexagonal.shared.domain.shoppingcart.api.ShoppingCartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShoppingCartEntity {
  private final UUID id;
  private final List<ShoppingCartItem> items;

  public ShoppingCartEntity( final UUID id ) {
    this.id = id;
    items = new ArrayList<>();
  }

  public ShoppingCartEntity( final UUID id, List<ShoppingCartItem> items ) {
    this.id = id;
    this.items = items;
  }

  public void addItem( final ShoppingCartItem item ) {
    items.add( item );
  }

  public void removeItem( final UUID itemId ) {
    items.stream()
      .filter( item -> item.getId().equals( itemId ) )
      .findFirst()
      .ifPresent( items::remove );
  }

  public List<ShoppingCartItem> getItems() {
    return items;
  }

  public UUID getId() {
    return id;
  }
}
