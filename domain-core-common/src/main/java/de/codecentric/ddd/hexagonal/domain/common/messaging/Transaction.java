package de.codecentric.ddd.hexagonal.domain.common.messaging;

import lombok.extern.java.Log;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Log
public class Transaction<T, U> {
  public final  TransactionResult<U> result;
  private final UUID                 correlationId;
  private final Messagebus           eventbus;
  private final Messagebus           commandbus;
  private final Message<T>           command;
  private final Class<?>             eventTypeToWaitFor;
  private final Message<String>      timeoutEvent;

  public Transaction( final Messagebus eventbus,
                      final Messagebus commandbus,
                      final Message<T> command,
                      final Class<?> eventTypeToWaitFor,
                      final Message<String> transactionFailedEvent ) {
    this.eventTypeToWaitFor = eventTypeToWaitFor;
    this.timeoutEvent = transactionFailedEvent;
    this.result = new TransactionResult<>();
    this.eventbus = eventbus;
    this.commandbus = commandbus;
    this.correlationId = command.getCorrelationId().orElseThrow();
    this.command = command;
  }

  private void register() {
    eventbus.register( this.eventTypeToWaitFor, this::handleEvent );
  }

  private void unregister() {
    eventbus.unregister( this.eventTypeToWaitFor, this::handleEvent );
  }

  private void handleEvent( final Message<?> msg ) {
    msg.getCorrelationId()
      .ifPresent( correlationId -> {
        if( correlationId.equals( this.correlationId ) ) {
          synchronized( result ) {
            //noinspection unchecked
            result.complete( (U) msg.getPayload() );
            result.notify();
          }
        }
      } );
  }

  private void send() {
    commandbus.send( command );
  }

  private void waitForTransactionResult() {
    while( !result.complete ) {
      try {
        synchronized( result ) {
          result.wait( 100 );
        }
      } catch( final InterruptedException e ) {
        log.warning( "Exception while waiting for transaction result: "+e.getMessage() );
      }
    }
  }

  public Optional<U> getResult() {
    return Optional.ofNullable( result.result );
  }

  private Void timeoutHandler( final Throwable e ) {
    eventbus.send( timeoutEvent );
    return null;
  }

  public CompletableFuture<U> run() {
    return CompletableFuture
             .runAsync( this::register )
             .thenRunAsync( this::send )
             .runAfterBothAsync(
               CompletableFuture.runAsync( this::waitForTransactionResult )
                 .orTimeout( 3, TimeUnit.SECONDS )
                 .exceptionally( this::timeoutHandler )
               , this::unregister )
             .thenApply( ignore -> this.getResult().get());
  }
}

class TransactionResult<T> {
  public boolean complete = false;
  public T       result;

  public void complete( final T result ) {
    this.complete = true;
    this.result = result;
  }
}
