package de.codecentric.ddd.hexagonal.monolith.order;

import de.codecentric.ddd.hexagonal.monolith.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.monolith.domain.order.api.OrdersApi;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class OrderController {
  private final OrdersApi api;

  public OrderController( final OrdersApi api ) {
    this.api = api;
  }

  @GetMapping( "/order" )
  public List<Order> getOrders() {
    return api.getOrders();
  }

  @PostMapping( "/order" )
  public void postOrder( @RequestBody final Order order ) {
    api.createOrder( order );
  }
}
