package de.codecentric.ddd.hexagonal.domain.common.messaging;

import static java.util.stream.Collectors.toUnmodifiableList;
import lombok.extern.java.Log;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log
public class MessagebusLocal implements Messagebus {
  private final HashMap<Class<?>, Set<MessageHandler<Message<?>>>> handlers;

  public MessagebusLocal() {
    handlers = new HashMap<>();
  }

  @Override public <T, U extends Message<T>> void send( final U message ) {
    final Set<MessageHandler<Message<?>>> messageHandlers = handlers.getOrDefault( message.getClass(),
                                                                                   Collections.emptySet() );
    final List<CompletableFuture<Void>> futures = messageHandlers.stream()
                                                    .map( messageHandler -> CompletableFuture.runAsync(
                                                      () -> messageHandler.accept( message ) ) )
                                                    .collect( toUnmodifiableList() );
    try {
      CompletableFuture.allOf( futures.toArray( CompletableFuture[]::new ) ).get();
    } catch( InterruptedException|ExecutionException e ) {
      log.warning(
        "Sending "+message.getType()+" caused an exception to be thrown: "+e.getMessage()+" \nContinuing gracefully." );
    }
  }

  @Override public void register( Class<?> type, final MessageHandler<Message<?>> handler ) {
    if( !handlers.containsKey( type ) ) {
      handlers.put( type, new HashSet<>() );
    }
    handlers.get( type ).add( handler );
  }

  @Override public void unregister( Class<?> type, final MessageHandler<Message<?>> handler ) {
    if( !handlers.containsKey( type ) ) {
      handlers.get( type ).remove( handler );
    }
  }

  @Override public void unregisterAll( Class<?> type ) {
    handlers.remove( type );
  }

  @Override public void unregisterAll() {
    handlers.keySet().stream().collect(toUnmodifiableList()).forEach( handlers::remove );
  }
}
