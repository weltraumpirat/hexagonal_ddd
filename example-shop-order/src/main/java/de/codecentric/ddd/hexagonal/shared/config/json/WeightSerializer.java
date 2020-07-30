package de.codecentric.ddd.hexagonal.shared.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.codecentric.ddd.hexagonal.domain.order.api.Weight;

import java.io.IOException;

public class WeightSerializer extends StdSerializer<Weight> {
  public WeightSerializer() {
    this( Weight.class );
  }

  public WeightSerializer( final Class<Weight> t ) {
    super( t );
  }

  @Override public void serialize( final Weight value, final JsonGenerator gen, final SerializerProvider provider )
    throws IOException {
    gen.writeString( value.toString() );
  }
}
