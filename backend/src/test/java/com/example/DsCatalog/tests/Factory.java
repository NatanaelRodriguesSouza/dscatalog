package com.example.DsCatalog.tests;

import com.example.DsCatalog.Dto.ProductDTO;
import com.example.DsCatalog.entities.Category;
import com.example.DsCatalog.entities.Product;

import java.time.Instant;

public class Factory {
    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(new Category(1L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static Category createCategory(){
        Category category = new Category(1L,"Computadores");
        return category;
    }
}
