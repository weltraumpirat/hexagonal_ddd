package de.codecentric.ddd.hexagonal.product.persistence;

import de.codecentric.ddd.hexagonal.domain.product.api.ProductListRepository;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductListRow;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProductListRepositoryInMemory implements ProductListRepository {
  private final HashMap<UUID, ProductListRow> products;

  public ProductListRepositoryInMemory() {
    this.products = new HashMap<>();
  }

  @Override public List<ProductListRow> findAll() {
    return products.values().stream().collect( toUnmodifiableList() );
  }

  @Override public void create( final ProductListRow row ) {
    products.put( row.getId(), row );
  }

  @Override public void delete( final UUID productId ) {
    products.remove( productId );
  }
}
