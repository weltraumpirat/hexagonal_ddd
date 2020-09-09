package de.codecentric.ddd.hexagonal.shared.product.persistence;

import de.codecentric.ddd.hexagonal.domain.product.api.Product;
import de.codecentric.ddd.hexagonal.domain.product.api.ProductRepository;
import static de.codecentric.ddd.hexagonal.shared.config.json.AmountMapper.toAmount;
import static de.codecentric.ddd.hexagonal.shared.config.json.MoneyMapper.toMoney;
import org.joda.money.Money;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductRepositoryJpa implements ProductRepository {
  private final ProductCrudRepository jpaRepo;

  public ProductRepositoryJpa( final ProductCrudRepository jpaRepo ) {
    this.jpaRepo = jpaRepo;
  }

  @Override public void create( final Product product ) {
    ProductEntity p = new ProductEntity();
    p.setId(product.getId());
    p.setName(product.getName());
    p.setPackagingType( product.getPackagingType() );
    final Money money = product.getPrice();
    p.setPrice(money.getCurrencyUnit()+" "+money.getAmount().toString());
    p.setAmount( product.getAmount().toString() );
    jpaRepo.save( p );
  }
            @Transactional
  @Override public void delete( final UUID id ) {
    jpaRepo.deleteById( id );
  }

  @Override public List<Product> findAll() {
    final Iterable<ProductEntity> entities = jpaRepo.findAll();
    return StreamSupport.stream( entities.spliterator(), false )
                                     .map( this::entityToProduct )
                                     .collect( Collectors.toList() );
  }

  private Product entityToProduct(ProductEntity e) {
    return new Product(
      e.getId(),
      e.getName(),
      e.getPackagingType(),
      toMoney( e.getPrice() ),
      toAmount( e.getAmount() )
    );
  }

  @Override public Product findById( final UUID id ) {
    return jpaRepo.findById( id ).map(this::entityToProduct).orElseThrow();
  }
}
