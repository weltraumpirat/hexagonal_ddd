package de.codecentric.ddd.hexagonal.shared.product.persistence;

import de.codecentric.ddd.hexagonal.domain.product.api.ProductShoppingListRepository;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductShoppingListRow;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class ProductShoppingListRepositoryJpa implements ProductShoppingListRepository {
  private final ProductShoppingListCrudRepository crudRepository;

  public ProductShoppingListRepositoryJpa(
    final ProductShoppingListCrudRepository crudRepository ) {
    this.crudRepository = crudRepository;
  }

  @Override public List<ProductShoppingListRow> findAll() {
    return StreamSupport.stream(crudRepository.findAll().spliterator(), false)
             .map(ProductFactory::create )
             .collect( toUnmodifiableList() );
  }

  @Override public void create( final ProductShoppingListRow product ) {
    crudRepository.save(ProductFactory.createShoppingListRowEntity( product ));
  }

  @Override public void delete( final UUID productId ) {
    crudRepository.deleteById( productId );
  }
}
