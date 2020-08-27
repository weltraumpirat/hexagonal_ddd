package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.UUID;

public interface ProductsCommandsApi {
  void addProduct( Product product );

  void removeProduct( UUID id );
}
