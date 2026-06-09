package com.biomarket.demo.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class ProductValidationTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void productRejectsZeroPrice() {
        Product product = validProduct();
        product.setPrice(BigDecimal.ZERO);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("price"));
    }

    @Test
    void productRejectsNegativeStock() {
        Product product = validProduct();
        product.setStock(-1);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("stock"));
    }

    private Product validProduct() {
        Product product = new Product();
        product.setName("Tomate organico");
        product.setDescription("Tomate cultivado sin quimicos.");
        product.setPrice(new BigDecimal("1200.00"));
        product.setStock(10);
        return product;
    }
}
