package de.codecentric.ddd.hexagonal.product.persistence;

import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductNotFoundException;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductValidationRepository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ProductValidationRepositoryInMemory implements ProductValidationRepository {
  private final HashMap<UUID, Product> products;

  public ProductValidationRepositoryInMemory() {
    products = new HashMap<>();
  }

  @Override public Product findByLabel( final String label ) {
    final Optional<Product>
      product = this.products.values().stream()
                  .filter( ( Product p ) -> p.toLabel().equals( label ) )
                  .findAny();
    return product.orElseThrow( ProductNotFoundException::new );
  }

  @Override public void create( final Product product ) {
    this.products.put( product.getId(), product );
  }

  @Override public void delete( final UUID id ) {
    this.products.remove( id );
  }
}
