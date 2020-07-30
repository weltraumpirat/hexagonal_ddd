package de.codecentric.ddd.hexagonal.shared.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import static de.codecentric.ddd.hexagonal.shared.config.json.AmountMapper.toAmount;
import de.codecentric.ddd.hexagonal.shared.domain.product.api.Amount;

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
    return toAmount( amount );
  }

}
