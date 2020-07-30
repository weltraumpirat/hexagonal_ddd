package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.List;
import java.util.UUID;

public interface ProductRepository {
  void create( Product product );

  void delete( UUID id );

  List<Product> findAll();
}
