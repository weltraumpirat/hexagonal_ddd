package de.codecentric.ddd.hexagonal.shared.domain.product.impl;

import de.codecentric.ddd.hexagonal.shared.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.shared.domain.product.api.ProductsApi;
import de.codecentric.ddd.hexagonal.shared.domain.product.api.ProductRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductsApiImpl implements ProductsApi {
  private final ProductRepository repository;

  public ProductsApiImpl( ProductRepository repository ) {
    this.repository = repository;
  }

  @Override public void addProduct( final Product product ) {
    repository.create( product );
  }

  @Override public void removeProduct( final UUID id ) {
    repository.delete( id );
  }

  @Override public List<Product> getProducts() {
    return repository.findAll().stream()
             .collect( Collectors.toUnmodifiableList() );
  }
}
