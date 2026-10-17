package com.cinema.application.port;

import com.cinema.domain.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductPort {
    Optional<Product> findById(Long id);
    Product save(Product product);
    List<Product> findAll();
}
