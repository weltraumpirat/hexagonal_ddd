package de.codecentric.ddd.hexagonal.domain.common.messaging;

import java.util.UUID;

public class Command<T> extends Message<T> {
  public Command( final String type, final T payload ) {
    super( type, payload );
  }

  public Command( final UUID correlationId, final String type, final T payload ) {
    super( correlationId, type, payload );
  }
}
