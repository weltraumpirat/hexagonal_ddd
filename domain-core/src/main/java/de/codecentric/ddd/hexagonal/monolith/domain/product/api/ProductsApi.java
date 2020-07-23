package de.codecentric.ddd.hexagonal.monolith.domain.product.api;

import java.util.List;
import java.util.UUID;

public interface ProductsApi {
  List<Product> getProducts();

  void addProduct( Product product );

  void removeProduct( UUID id );

}
