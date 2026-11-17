package com.cinema.domain.repository;

import com.cinema.domain.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    Product save(Product product);
    List<Product> findAll();
}
