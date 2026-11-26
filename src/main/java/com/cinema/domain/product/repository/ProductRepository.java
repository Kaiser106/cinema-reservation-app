package com.cinema.domain.product.repository;

import com.cinema.domain.product.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    Product save(Product product);
    List<Product> findAll();
}
