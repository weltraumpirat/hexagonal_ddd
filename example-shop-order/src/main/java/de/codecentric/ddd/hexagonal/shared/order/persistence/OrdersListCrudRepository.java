package de.codecentric.ddd.hexagonal.shared.order.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface OrdersListCrudRepository extends CrudRepository<OrdersListRowEntity, UUID> {
  @Query("SELECT e FROM OrdersListRowEntity e ORDER BY e.timestamp DESC")
  List<OrdersListRowEntity> findAll();
}
