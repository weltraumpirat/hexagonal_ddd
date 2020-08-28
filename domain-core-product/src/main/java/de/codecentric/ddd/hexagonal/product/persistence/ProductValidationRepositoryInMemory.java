package de.codecentric.ddd.hexagonal.product.persistence;

import de.codecentric.ddd.hexagonal.domain.product.api.ProductNotFoundException;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductValidationEntry;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductValidationRepository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ProductValidationRepositoryInMemory implements ProductValidationRepository {
  private final HashMap<UUID, ProductValidationEntry> products;

  public ProductValidationRepositoryInMemory() {
    products = new HashMap<>();
  }

  @Override public ProductValidationEntry findByLabel( final String label ) {
    final Optional<ProductValidationEntry>
      product = this.products.values().stream()
                  .filter( ( ProductValidationEntry p ) -> p.getLabel().equals( label ) )
                  .findAny();
    return product.orElseThrow( ProductNotFoundException::new );
  }

  @Override public void create( final ProductValidationEntry product ) {
    this.products.put( product.getId(), product );
  }

  @Override public void delete( final UUID id ) {
    this.products.remove( id );
  }
}
