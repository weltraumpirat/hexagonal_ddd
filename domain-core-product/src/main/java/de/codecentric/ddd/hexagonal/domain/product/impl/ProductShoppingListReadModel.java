package de.codecentric.ddd.hexagonal.domain.product.impl;

import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductShoppingListRepository;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductShoppingListRow;
import org.joda.money.Money;

import java.util.List;
import java.util.UUID;

public class ProductShoppingListReadModel {
  private final ProductShoppingListRepository repository;

  public ProductShoppingListReadModel(
    final ProductShoppingListRepository  repository ) {
    this.repository = repository;
  }

  public List<ProductShoppingListRow> read() {
    return repository.findAll();
  }

  public void onProductCreated( final Product product ) {
    final ProductShoppingListRow listRow = new ProductShoppingListRow( product.getId(),
                                                       product.getName(),
                                                       product.getPackagingType().toString(),
                                                       product.getAmount().toString(),
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
