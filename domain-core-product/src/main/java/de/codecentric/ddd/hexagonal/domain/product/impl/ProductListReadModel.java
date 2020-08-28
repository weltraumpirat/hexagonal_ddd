package de.codecentric.ddd.hexagonal.domain.product.impl;

import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductListRepository;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductListRow;
import org.joda.money.Money;

import java.util.List;
import java.util.UUID;

public class ProductListReadModel {
  private final ProductListRepository repository;

  public ProductListReadModel(
    final ProductListRepository repository ) {
    this.repository = repository;
  }

  public List<ProductListRow> read() {
    return repository.findAll();
  }

  public void onProductCreated( final Product product ) {
    final ProductListRow listRow = new ProductListRow( product.getId(),
                                                       product.toLabel(),
                                                       toMoneyString( product.getPrice() ) );
    repository.create( listRow );
  }

  private String toMoneyString( final Money price ) {
    return price.getCurrencyUnit()+" "+
           price.getAmount();
  }

  public void onProductRemoved( final UUID productId ) {
    repository.delete( productId );
  }
}
