package de.codecentric.ddd.hexagonal.shared.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.codecentric.ddd.hexagonal.domain.common.messaging.Message;
import lombok.extern.java.Log;

@Log
public class MessageLogger {
  private final ObjectMapper mapper;

  public MessageLogger( final ObjectMapper mapper ) {
    this.mapper = mapper;
  }

  public void onMessage( final Message<?> msg ) {
    try {
      log.info( msg.getType()+":"+msg.getCorrelationId()+" -- "+mapper.writeValueAsString( msg.getPayload() ) );
    } catch( JsonProcessingException e ) {
      log.warning( e.getMessage() );
    }
  }
}
