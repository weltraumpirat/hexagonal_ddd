package de.codecentric.ddd.hexagonal.domain.shoppingcart.api;

import de.codecentric.ddd.hexagonal.domain.shoppingcart.impl.ShoppingCartListRow;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartListRowRepository {
  void create( ShoppingCartListRow row );

  void delete( UUID id );

  void update( ShoppingCartListRow row );

  List<ShoppingCartListRow> findAll();
}
