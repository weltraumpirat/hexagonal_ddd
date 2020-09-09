package de.codecentric.ddd.hexagonal.shared.product.persistence;

import de.codecentric.ddd.hexagonal.domain.product.api.ProductNotFoundException;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductValidationEntry;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductValidationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductValidationRepositoryJpa implements ProductValidationRepository {
  private final ProductValidationCrudRepository crudRepository;

  public ProductValidationRepositoryJpa(
    final ProductValidationCrudRepository crudRepository ) {
    this.crudRepository = crudRepository;
  }

  @Override public void create( final ProductValidationEntry product ) {
     crudRepository.save(ProductFactory.createValidationEntity(product));
  }

  @Override public void delete( final UUID id ) {
     crudRepository.deleteById( id );
  }

  @Override public ProductValidationEntry findByLabel( final String label ) {
    return Optional.ofNullable( crudRepository.findByLabel(label) )
             .map(ProductFactory::create)
             .orElseThrow( ProductNotFoundException::new );
  }
}
