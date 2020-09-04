package de.codecentric.ddd.hexagonal.domain.common.messaging;

public class TransactionResult<T> {
  public boolean complete = false;
  public T       payload;

  public void complete( final T payload ) {
    this.payload = payload;
    this.complete = true;
  }
}
