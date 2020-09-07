package de.codecentric.ddd.hexagonal.domain.product.messaging;

import de.codecentric.ddd.hexagonal.domain.common.messaging.Command;

import java.util.UUID;

public class RemoveProductCommand extends Command<UUID> {

  public static final String REMOVE_PRODUCT = "REMOVE_PRODUCT";

  public RemoveProductCommand( final UUID payload ) {
    super( REMOVE_PRODUCT, payload );
  }

  public RemoveProductCommand( final UUID correlationId, final UUID payload ) {
    super( correlationId, REMOVE_PRODUCT, payload );
  }
}
