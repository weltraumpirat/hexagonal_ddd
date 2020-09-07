package de.codecentric.ddd.hexagonal.domain.common.messaging;

public class TransactionFactory {
  private final Messagebus eventbus;
  private final Messagebus commandbus;

  public TransactionFactory( final Messagebus eventbus,
                             final Messagebus commandbus ) {
    this.eventbus = eventbus;
    this.commandbus = commandbus;
  }

  public <T> Transaction<T> create( final Message<T> command,
                                           final Class<?> waitForEvent,
                                           final Message<String> transactionFailedEvent ) {
    return new Transaction<>( eventbus, commandbus, command, waitForEvent, transactionFailedEvent );
  }
}
