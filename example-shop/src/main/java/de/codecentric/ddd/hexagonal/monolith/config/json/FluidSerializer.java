package de.codecentric.ddd.hexagonal.monolith.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Fluid;

import java.io.IOException;

public class FluidSerializer extends StdSerializer<Fluid> {
  public FluidSerializer() {
    this(Fluid.class);
  }

  public FluidSerializer( final Class<Fluid> t ) {
    super( t );
  }

  @Override public void serialize( final Fluid value, final JsonGenerator gen, final SerializerProvider provider )
    throws IOException {
    gen.writeString( value.toString() );
  }
}
