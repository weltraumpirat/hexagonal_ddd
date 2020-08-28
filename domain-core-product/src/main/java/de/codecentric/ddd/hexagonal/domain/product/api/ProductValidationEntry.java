package de.codecentric.ddd.hexagonal.domain.product.api;

import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor( access = AccessLevel.PRIVATE, force = true )
public class ProductValidationEntry {
  private final UUID          id;
  private final String        label;
}
