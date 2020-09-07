package de.codecentric.ddd.hexagonal.domain.product.impl;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import de.codecentric.ddd.hexagonal.domain.common.messaging.Messagebus;
import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductValidationEntry;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductValidationRepository;
import de.codecentric.ddd.hexagonal.domain.product.messaging.ProductCreatedEvent;
import de.codecentric.ddd.hexagonal.domain.product.messaging.ProductRemovedEvent;

import java.util.UUID;

public class ProductValidationReadModel {
  private final ProductValidationRepository validationRepository;
  private final Messagebus eventbus;

  public ProductValidationReadModel(
    final ProductValidationRepository validationRepository, final Messagebus eventbus ) {
    this.validationRepository = validationRepository;
    this.eventbus = eventbus;
    eventbus.register( ProductCreatedEvent.class, this::onProductCreated);
    eventbus.register( ProductRemovedEvent.class, this::onProductRemoved );
  }

  public ProductValidationEntry read( final String label ) {
    return validationRepository.findByLabel( label );
  }

  public void onProductCreated( final Message<?> msg ) {
    final Product product = ((ProductCreatedEvent) msg).getPayload();
    validationRepository.create( new ProductValidationEntry( product.getId(), product.toLabel() ) );
  }

  public void onProductRemoved( final Message<?> msg ) {
    final UUID productId = ( (ProductRemovedEvent) msg ).getPayload();
    validationRepository.delete( productId );
  }
}
