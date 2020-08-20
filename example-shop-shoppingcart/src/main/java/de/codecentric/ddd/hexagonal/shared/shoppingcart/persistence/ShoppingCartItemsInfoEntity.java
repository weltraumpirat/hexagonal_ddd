package de.codecentric.ddd.hexagonal.shared.shoppingcart.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItemsInfoEntity {
  @Id
  @Column( length = 16, nullable = false, unique = true )
  private UUID id;
  @Column( length = 100000)
  private String items;
  private int count;
  private String total;
}
