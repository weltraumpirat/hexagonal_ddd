package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.List;
import java.util.UUID;

public interface ProductListRepository {
  List<ProductListRow> findAll();

  void create( ProductListRow product );

  void delete( UUID productId );
}
