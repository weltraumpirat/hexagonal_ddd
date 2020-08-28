package de.codecentric.ddd.hexagonal.shared.product.persistence;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductShoppingListRowEntity {
  @Id
  @Column( length = 16, nullable = false, unique = true )
  private UUID   id;
  private String name;
  @Column( length = 32 )
  private String packagingType;
  @Column( length = 12 )
  private String price;
  @Column( length = 10 )
  private String amount;
}

