package de.codecentric.ddd.hexagonal.domain.common.messaging;

import lombok.Data;

@Data
public class Message<T> {
  private final String type;
  private final T payload;

  public Message( final String type, final T payload ) {
    this.type = type;
    this.payload = payload;
  }
}
