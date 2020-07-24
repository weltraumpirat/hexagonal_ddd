package de.codecentric.ddd.hexagonal.monolith.config.json;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;

public class MoneyMapper {
  public static Money toMoney( final String text ) {
    final String[] price = text.split( " " );
    final CurrencyUnit cur = CurrencyUnit.of( price[0] );
    final BigDecimal value = new BigDecimal( price[1] );
    return Money.of( cur, value );
  }
}
