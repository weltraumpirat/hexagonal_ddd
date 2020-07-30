package de.codecentric.ddd.hexagonal.shared.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.codecentric.ddd.hexagonal.domain.product.api.PackagingType;

import java.io.IOException;

public class PackagingTypeSerializer extends StdSerializer<PackagingType> {
  public PackagingTypeSerializer() {
    this(PackagingType.class);
  }

  public PackagingTypeSerializer( final Class<PackagingType> t ) {
    super( t );
  }

  @Override public void serialize( final PackagingType value, final JsonGenerator gen, final SerializerProvider provider )
    throws IOException {
    gen.writeString( value.toString() );
  }
}
