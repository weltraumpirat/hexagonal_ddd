package de.codecentric.ddd.hexagonal.monolith.config.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.money.Money;

public class MoneyModule extends SimpleModule {
  public MoneyModule() {
    super("MoneyModule");
    this.addSerializer( Money.class, new MoneySerializer() );
    this.addDeserializer( Money.class, new MoneyDeserializer() );
  }
}
