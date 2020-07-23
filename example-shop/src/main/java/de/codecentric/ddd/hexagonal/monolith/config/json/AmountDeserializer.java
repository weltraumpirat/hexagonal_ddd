package de.codecentric.ddd.hexagonal.monolith.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Amount;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Fluid;

import java.io.IOException;

public class AmountDeserializer extends StdDeserializer<Amount> {
  public AmountDeserializer() {
    this( Amount.class );
  }

  public AmountDeserializer( final Class<Amount> vc ) {
    super( vc );
  }

  @Override public Amount deserialize( final JsonParser p, final DeserializationContext ctxt )
    throws IOException {
    JsonNode node = p.getCodec().readTree( p );
    final String amount = node.asText();
    switch(amount) {
      case "0.5l":
      case "1l":
        return Fluid.forValue( amount );
    }
    return null;
  }
}
