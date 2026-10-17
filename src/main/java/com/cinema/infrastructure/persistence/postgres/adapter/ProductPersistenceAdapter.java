package com.cinema.infrastructure.persistence.postgres.adapter;

import com.cinema.application.port.ProductPort;
import com.cinema.domain.common.Money;
import com.cinema.domain.product.Product;
import com.cinema.infrastructure.persistence.postgres.entity.ProductEntity;
import com.cinema.infrastructure.persistence.postgres.repository.ProductJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductPersistenceAdapter implements ProductPort {
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
