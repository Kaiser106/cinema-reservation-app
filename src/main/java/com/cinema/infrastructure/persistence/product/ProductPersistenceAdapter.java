package com.cinema.infrastructure.persistence.product;

import com.cinema.domain.product.model.Product;
import com.cinema.domain.product.repository.ProductRepository;
import com.cinema.domain.shared.vobj.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductPersistenceAdapter implements ProductRepository {
    private final ProductJpaRepository repo;

    public ProductPersistenceAdapter(ProductJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repo.findById(id).map(e -> new Product(e.getId(), e.getName(), Money.of(e.getPrice()), e.getStock()));
    }

    @Override
    public Product save(Product product) {
        ProductEntity e = new ProductEntity();
        e.setId(product.getId());
        e.setName(product.getName());
        e.setPrice(product.getPrice().toBigDecimal());
        e.setStock(product.getStock());
        ProductEntity saved = repo.save(e);
        return new Product(saved.getId(), saved.getName(), Money.of(saved.getPrice()), saved.getStock());
    }

    @Override
    public List<Product> findAll() {
        return repo.findAll().stream().map(e -> new Product(e.getId(), e.getName(), Money.of(e.getPrice()), e.getStock())).toList();
    }
}
