package de.codecentric.ddd.hexagonal.shared.order.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class OrderEntity {
  @Id
  @Column(length=16, nullable = false, unique = true)
  private UUID      id;
  private String        total;
  @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime timestamp;

  public OrderEntity(final UUID id, final String total) {
    this.id = id;
    this.total = total;
  }
}
