package de.codecentric.ddd.hexagonal.domain.common.messaging;

import java.util.UUID;

public class Event<T> extends Message<T>{
  public Event( final String type, final T payload ) {
    super( type, payload );
  }

  public Event( final UUID correlationId, final String type, final T payload ) {
    super( correlationId, type, payload );
  }
}
