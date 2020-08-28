package de.codecentric.ddd.hexagonal.domain.product.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductShoppingListRow {
  private UUID   id;
  private String name;
  private String packagingType;
  private String amount;
  private String price;
}
