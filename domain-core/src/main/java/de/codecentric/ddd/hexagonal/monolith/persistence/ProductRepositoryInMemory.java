package de.codecentric.ddd.hexagonal.monolith.persistence;

import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductRepositoryInMemory implements ProductRepository {

  private final Map<UUID, Product> products;

  public ProductRepositoryInMemory() {
    products = new HashMap<>();
  }

  @Override public void create( final Product product ) {
    products.put( product.getId(), product );
  }

  @Override public void delete( final UUID id ) {
    products.remove( id );
  }

  @Override public List<Product> findAll() {
    return products.values().stream()
             .collect( Collectors.toUnmodifiableList() );
  }
}
