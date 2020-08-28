package de.codecentric.ddd.hexagonal.shared.product.persistence;

import de.codecentric.ddd.hexagonal.domain.product.api.ProductListRepository;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductListRow;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class ProductListRepositoryJpa implements ProductListRepository {
  private final ProductListCrudRepository crudRepository;

  public ProductListRepositoryJpa(
    final ProductListCrudRepository crudRepository ) {
    this.crudRepository = crudRepository;
  }

  @Override public List<ProductListRow> findAll() {
    return StreamSupport.stream(crudRepository.findAll().spliterator(), false)
             .map(ProductFactory::create )
             .collect( toUnmodifiableList() );
  }

  @Override public void create( final ProductListRow product ) {
    crudRepository.save(ProductFactory.createListRowEntity( product ));
  }

  @Override public void delete( final UUID productId ) {
    crudRepository.deleteById( productId );
  }
}
