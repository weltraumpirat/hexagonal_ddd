package de.codecentric.ddd.hexagonal.monolith.order.persistence;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class OrderPositionEntity {
  @Id
  @Column(length=16, nullable = false, unique = true)
  private       UUID   id;
  @Column(length=16, nullable = false)
  private       UUID   orderId;
  private String itemName;
  private int    count;
  @Column( length=20)
  private String  singlePrice;
  @Column( length=20)
  private String  combinedPrice;
}
