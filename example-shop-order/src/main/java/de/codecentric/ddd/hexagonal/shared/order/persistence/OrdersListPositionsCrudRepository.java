package de.codecentric.ddd.hexagonal.shared.order.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrdersListPositionsCrudRepository extends CrudRepository<OrdersListRowPositionEntity, UUID> {
}
