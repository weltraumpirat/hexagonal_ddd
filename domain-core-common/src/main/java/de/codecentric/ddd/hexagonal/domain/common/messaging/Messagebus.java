package de.codecentric.ddd.hexagonal.domain.common.messaging;


public interface Messagebus {
  <T, U extends Message<T>>void send( U message );

  void register( final Class<?> type, final MessageHandler<Message<?>> handler );

  void unregister( final Class<?> type, final MessageHandler<Message<?>> handler );

  void unregisterAll( final Class<?> type );
  void unregisterAll( );
}
