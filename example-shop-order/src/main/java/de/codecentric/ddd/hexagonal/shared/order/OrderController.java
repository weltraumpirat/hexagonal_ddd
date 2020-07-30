package de.codecentric.ddd.hexagonal.shared.order;

import de.codecentric.ddd.hexagonal.domain.order.api.Order;
import de.codecentric.ddd.hexagonal.domain.order.api.OrdersApi;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001","http://localhost" })
@RestController
public class OrderController {
  private final OrdersApi api;

  public OrderController( final OrdersApi api ) {
    this.api = api;
  }

  @GetMapping( "/api/order" )
  public List<Order> getOrders() {
    return api.getOrders();
  }

  @PostMapping( "/api/order" )
  public void postOrder( @RequestBody final Order order ) {
    api.createOrder( order );
  }
}
