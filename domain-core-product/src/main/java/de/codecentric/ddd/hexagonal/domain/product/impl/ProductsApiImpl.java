package de.codecentric.ddd.hexagonal.domain.product.impl;


import de.codecentric.ddd.hexagonal.domain.product.api.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductsApiImpl implements ProductsApi {
  private final ProductRepository repository;
  private final ProductValidationReadModel validationReadModel;
  private final ProductListReadModel productListReadModel;

  public ProductsApiImpl( ProductRepository repository,
                          final ProductValidationReadModel validationReadModel,
                          final ProductListReadModel productListReadModel ) {
    this.repository = repository;
    this.validationReadModel = validationReadModel;
    this.productListReadModel = productListReadModel;
  }

  @Override public void addProduct( final Product product ) {
    repository.create( product );
    validationReadModel.onProductCreated(product);
    productListReadModel.onProductCreated(product);
  }

  @Override public void removeProduct( final UUID id ) {
    repository.delete( id );
    validationReadModel.onProductRemoved(id);
    productListReadModel.onProductRemoved(id);
  }

  @Override public List<Product> getProducts() {
    return repository.findAll().stream()
             .collect( Collectors.toUnmodifiableList() );
  }

  @Override public List<ProductListRow> getProductList() {
    return productListReadModel.read();
  }

  @Override public Product getProductById( final UUID id ) {
    final Optional<Product> product = Optional.ofNullable( repository.findById( id ));
    return product.orElseThrow();
  }

  @Override public ProductValidationEntry validateProduct( final String label ) {
    return validationReadModel.read(label);
  }
}
