package de.codecentric.ddd.hexagonal.domain.product.impl;


import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductRepository;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductsApi;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductsApiImpl implements ProductsApi {
  private final ProductRepository repository;
  private final ProductValidationReadModel validationReadModel;

  public ProductsApiImpl( ProductRepository repository,
                          final ProductValidationReadModel validationReadModel ) {
    this.repository = repository;
    this.validationReadModel = validationReadModel;
  }

  @Override public void addProduct( final Product product ) {
    repository.create( product );
    validationReadModel.onProductCreated(product);
  }

  @Override public void removeProduct( final UUID id ) {
    repository.delete( id );
    validationReadModel.onProductRemoved(id);
  }

  @Override public List<Product> getProducts() {
    return repository.findAll().stream()
             .collect( Collectors.toUnmodifiableList() );
  }

  @Override public Product getProductById( final UUID id ) {
    final Optional<Product> product = Optional.ofNullable( repository.findById( id ));
    return product.orElseThrow();
  }

  @Override public Product validateProduct( final String label ) {
    return validationReadModel.read(label);
  }
}
