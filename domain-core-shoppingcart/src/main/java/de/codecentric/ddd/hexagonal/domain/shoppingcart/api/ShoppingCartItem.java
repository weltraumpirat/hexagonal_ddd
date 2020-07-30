package de.codecentric.ddd.hexagonal.domain.shoppingcart.api;

import static lombok.AccessLevel.PRIVATE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.util.UUID;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor( access = PRIVATE, force = true )
public class ShoppingCartItem {
  private final UUID id;
  private final String        label;
  private final Money         price;
}
