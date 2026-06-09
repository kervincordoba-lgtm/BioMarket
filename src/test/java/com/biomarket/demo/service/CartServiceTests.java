package com.biomarket.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.biomarket.demo.model.CartItem;
import com.biomarket.demo.model.Product;
import com.biomarket.demo.repository.ProductRepository;

class CartServiceTests {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final CartService cartService = new CartService(productRepository);

    @Test
    void addProductUsesRequestedQuantity() {
        Product product = productWithStock(5);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        List<CartItem> cart = new ArrayList<>();

        cartService.addProduct(cart, 1, 3);

        assertThat(cart).hasSize(1);
        assertThat(cart.get(0).getQuantity()).isEqualTo(3);
        assertThat(cart.get(0).getSubtotal()).isEqualByComparingTo("3000.00");
    }

    @Test
    void addProductRejectsQuantityGreaterThanStock() {
        Product product = productWithStock(2);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> cartService.addProduct(new ArrayList<>(), 1, 3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Product productWithStock(int stock) {
        Product product = new Product();
        product.setIdProduct(1);
        product.setName("Lechuga");
        product.setPrice(new BigDecimal("1000.00"));
        product.setImage("lechuga.jpg");
        product.setStock(stock);
        return product;
    }
}
