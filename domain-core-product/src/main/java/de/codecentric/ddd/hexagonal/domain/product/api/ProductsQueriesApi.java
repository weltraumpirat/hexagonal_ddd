package de.codecentric.ddd.hexagonal.domain.product.api;

import java.util.List;
import java.util.UUID;

public interface ProductsQueriesApi {
  List<Product> getProducts();

  Product getProductById( UUID id );

  Product validateProduct( String label );
}
