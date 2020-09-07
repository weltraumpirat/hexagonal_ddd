package de.codecentric.ddd.hexagonal.domain.common.messaging;

import lombok.Data;

import java.util.Optional;
import java.util.UUID;

@Data
public class Message<T> {
  private final UUID correlationId;
  private final String type;
  private final T payload;

  public Message( final String type, final T payload ) {
    this.type = type;
    this.payload = payload;
    correlationId = null;
  }

  public Message( final UUID correlationId, final String type, final T payload) {
    this.correlationId = correlationId;
    this.type = type;
    this.payload = payload;
  }

  public Optional<UUID> getCorrelationId() {
    return Optional.ofNullable( correlationId );
  }
}
