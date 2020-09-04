package de.codecentric.ddd.hexagonal.domain.common.messaging;

public class Event<T> extends Message<T>{
  public Event( final String type, final T payload ) {
    super( type, payload );
  }
}
