package de.codecentric.ddd.hexagonal.shared.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.codecentric.ddd.hexagonal.shared.domain.product.api.PackagingType;

import java.io.IOException;

public class PackagingTypeDeserializer extends StdDeserializer<PackagingType> {
  public PackagingTypeDeserializer() {
    this( PackagingType.class );
  }

  public PackagingTypeDeserializer( final Class<PackagingType> vc ) {
    super( vc );
  }

  @Override public PackagingType deserialize( final JsonParser p, final DeserializationContext ctxt )
    throws IOException {
    JsonNode node = p.getCodec().readTree( p );
    final String type = node.asText();
    return PackagingType.forValue( type );
  }
}
