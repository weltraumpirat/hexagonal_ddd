package de.codecentric.ddd.hexagonal.shared.product.persistence;

import de.codecentric.ddd.hexagonal.domain.product.api.PackagingType;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class ProductEntity {
  @Id
  @Column( length=16, nullable = false, unique = true)
  private UUID          id;
  private String        name;
  @Enumerated(EnumType.STRING)
  @Column(length=24)
  private PackagingType packagingType;
  @Column( length=20)
  private String        price;
  @Column( length=10)
  private String        amount;
}
