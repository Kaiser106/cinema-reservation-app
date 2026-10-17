package com.cinema.domain.product;

import com.cinema.domain.common.Money;

public class Product {
    private final Long id;
    private final String name;
    private final Money price;
    private int stock;

    public Product(Long id, String name, Money price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void purchase(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("product.quantity.invalid");
        if (stock < qty) throw new IllegalStateException("product.stock.insufficient");
        stock -= qty;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Money getPrice() { return price; }
    public int getStock() { return stock; }
}
