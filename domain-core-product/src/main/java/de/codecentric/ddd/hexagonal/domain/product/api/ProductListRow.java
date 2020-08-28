package de.codecentric.ddd.hexagonal.domain.product.api;

import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor( access = AccessLevel.PRIVATE, force = true )
public class ProductListRow {
  private final UUID   id;
  private final String label;
  private final String price;
}
