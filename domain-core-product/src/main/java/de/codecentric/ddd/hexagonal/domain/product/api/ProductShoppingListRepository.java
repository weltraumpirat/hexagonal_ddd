package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.List;
import java.util.UUID;

public interface ProductShoppingListRepository {
  List<ProductShoppingListRow> findAll();

  void create( ProductShoppingListRow listRow );

  void delete( UUID productId );
}
