package de.codecentric.ddd.hexagonal.domain.product.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductListRepository;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductListRow;
import de.codecentric.ddd.hexagonal.domain.product.messaging.ProductCreatedEvent;
import de.codecentric.ddd.hexagonal.domain.product.messaging.ProductRemovedEvent;
import org.joda.money.Money;

import java.util.List;
import java.util.UUID;

public class ProductListReadModel {
  private final ProductListRepository repository;

  public ProductListReadModel(
    final ProductListRepository repository, final Messagebus eventbus ) {
    this.repository = repository;
    eventbus.register( ProductCreatedEvent.class, this::onProductCreated );
    eventbus.register( ProductRemovedEvent.class, this::onProductRemoved );
  }

  public List<ProductListRow> read() {
    return repository.findAll();
  }

  public void onProductCreated( final Message<?> msg ) {
    final Product product = ( (ProductCreatedEvent) msg ).getPayload();
    final ProductListRow listRow = new ProductListRow( product.getId(),
                                                       product.toLabel(),
                                                       toMoneyString( product.getPrice() ) );
    repository.create( listRow );
  }

  private String toMoneyString( final Money price ) {
    return price.getCurrencyUnit()+" "+
           price.getAmount();
  }

  public void onProductRemoved( final Message<?> msg ) {
    final UUID productId = ( (ProductRemovedEvent) msg ).getPayload();
    repository.delete( productId );
  }
}
