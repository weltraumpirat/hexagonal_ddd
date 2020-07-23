package de.codecentric.ddd.hexagonal.monolith.product;

import de.codecentric.ddd.hexagonal.monolith.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.monolith.domain.product.api.ProductsApi;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ProductController {
  private final ProductsApi productApi;

  public ProductController( final ProductsApi productApi ) {
    this.productApi = productApi;
  }

  @GetMapping("/product")
  public List<Product> getProducts() {
    return productApi.getProducts().stream()
             .collect( Collectors.toUnmodifiableList() );
  }

  @PostMapping("/product")
  public void addProduct(@RequestBody final Product product ) {
    productApi.addProduct( product );
  }

  @DeleteMapping("/product")
  public void deleteProduct( final String id ) {
    productApi.removeProduct( UUID.fromString( id ) );
  }
}
