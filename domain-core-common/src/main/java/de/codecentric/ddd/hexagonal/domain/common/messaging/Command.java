package de.codecentric.ddd.hexagonal.domain.common.messaging;

public class Command<T> extends Message<T> {
  public Command( final String type, final T payload ) {
    super( type, payload );
  }
}
