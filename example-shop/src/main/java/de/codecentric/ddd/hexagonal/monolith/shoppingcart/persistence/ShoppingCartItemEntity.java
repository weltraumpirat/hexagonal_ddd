package de.codecentric.ddd.hexagonal.monolith.shoppingcart.persistence;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class ShoppingCartItemEntity {
  @Id
  @Column( length = 16, nullable = false, unique = true )
  private UUID   id;
  @Column( length = 16, nullable = false )
  private UUID   cartId;
  private String label;
  @Column( length = 10 )
  private String price;
}
