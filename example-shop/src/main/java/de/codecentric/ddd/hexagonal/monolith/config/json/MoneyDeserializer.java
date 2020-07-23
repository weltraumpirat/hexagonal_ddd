package de.codecentric.ddd.hexagonal.monolith.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.IOException;
import java.math.BigDecimal;

public class MoneyDeserializer extends StdDeserializer<Money> {
  public MoneyDeserializer() {
    this(Money.class);
  }
  public MoneyDeserializer( final Class<Money> vc ) {
    super( vc );
  }

  @Override public Money deserialize( final JsonParser p, final DeserializationContext ctxt )
    throws IOException {
    JsonNode node = p.getCodec().readTree( p );
    final String[] price = node.asText().split(" ");
    final CurrencyUnit cur = CurrencyUnit.of( price[0] );
    final BigDecimal value = new BigDecimal( price[1] );
    return Money.of(cur, value);
  }
}
