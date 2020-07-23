package de.codecentric.ddd.hexagonal.monolith.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.money.Money;

import java.io.IOException;

public class MoneySerializer extends StdSerializer<Money> {
  public MoneySerializer(){
    this(Money.class);
  }

  public MoneySerializer( final Class<Money> t ) {
    super( t );
  }

  @Override public void serialize( final Money money, final JsonGenerator generator, final SerializerProvider provider )
    throws IOException {
    generator.writeString( money.getCurrencyUnit()+" "+money.getAmount().toString() );
  }
}
