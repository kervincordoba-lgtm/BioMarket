package com.biomarket.demo.dto;

import com.biomarket.demo.model.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Integer idProduct,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String image,
        Integer categoryId,
        String categoryName) {

    public static ProductResponse from(Product product) {
        Integer categoryId = product.getCategory() != null ? product.getCategory().getIdCategory() : null;
        String categoryName = product.getCategory() != null ? product.getCategory().getName() : null;

        return new ProductResponse(
                product.getIdProduct(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImage(),
                categoryId,
                categoryName);
    }
}
