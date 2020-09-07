package de.codecentric.ddd.hexagonal.domain.product.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductRepository;
import de.codecentric.ddd.hexagonal.domain.product.messaging.AddProductCommand;
import de.codecentric.ddd.hexagonal.domain.product.messaging.ProductCreatedEvent;
import de.codecentric.ddd.hexagonal.domain.product.messaging.ProductRemovedEvent;
import de.codecentric.ddd.hexagonal.domain.product.messaging.RemoveProductCommand;

import java.util.UUID;

public class ProductsFixture {
  private final ProductRepository repository;
  private final Messagebus        eventbus;

  public ProductsFixture( final ProductRepository repository, final Messagebus eventbus,
                          final Messagebus commandbus ) {
    this.repository = repository;
    this.eventbus = eventbus;
    commandbus.register( AddProductCommand.class, this::onAddProduct );
    commandbus.register( RemoveProductCommand.class, this::onRemoveProduct );
  }

  public void onAddProduct( final Message<?> msg ) {
    final Product product = ( (AddProductCommand) msg ).getPayload();
    repository.create( product );
    msg.getCorrelationId()
      .ifPresentOrElse(
        correlationId -> eventbus.send( new ProductCreatedEvent( correlationId, product ) ),
        () -> eventbus.send( new ProductCreatedEvent( product ) ) );
  }

  public void onRemoveProduct( final Message<?> msg ) {
    final UUID id = ( (RemoveProductCommand) msg ).getPayload();
    repository.delete( id );
    msg.getCorrelationId()
      .ifPresentOrElse(
        correlationId -> eventbus.send( new ProductRemovedEvent( correlationId, id ) ),
        () -> eventbus.send( new ProductRemovedEvent( id ) ) );
  }
}
