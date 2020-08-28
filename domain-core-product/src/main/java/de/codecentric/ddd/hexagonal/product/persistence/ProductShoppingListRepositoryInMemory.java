package de.codecentric.ddd.hexagonal.product.persistence;

import de.codecentric.ddd.hexagonal.domain.product.api.ProductShoppingListRepository;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductShoppingListRow;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProductShoppingListRepositoryInMemory implements ProductShoppingListRepository {
  private final HashMap<UUID, ProductShoppingListRow> products;

  public ProductShoppingListRepositoryInMemory( ) {
    this.products = new HashMap<>();
  }

  @Override public List<ProductShoppingListRow> findAll() {
    return products.values().stream().collect( toUnmodifiableList() );
  }

  @Override public void create( final ProductShoppingListRow listRow ) {
    products.put( listRow.getId(), listRow );
  }

  @Override public void delete( final UUID productId ) {
    products.remove( productId );
  }
}
