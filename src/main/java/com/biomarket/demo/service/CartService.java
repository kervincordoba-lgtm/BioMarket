package com.biomarket.demo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.biomarket.demo.model.CartItem;
import com.biomarket.demo.model.Product;
import com.biomarket.demo.repository.ProductRepository;

@Service
public class CartService {
    
    private static final String DEFAULT_IMAGE = "fruite-item-1.jpg";
    private final ProductRepository productRepository;

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(List<CartItem> cart, Integer productId) {
        addProduct(cart, productId, 1);
    }

    public void addProduct(List<CartItem> cart, Integer productId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("Selecciona una cantidad valida.");
        }

        // 1. Buscar si el producto ya está en el carrito con un bucle clásico
        CartItem existingItem = null;
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                existingItem = item;
                break; 
            }
        }

        // 2. Si el objeto ya no es null, significa que ya existía en el carrito
        if (existingItem != null) {
            int requestedQuantity = existingItem.getQuantity() + quantity;

            if (requestedQuantity <= existingItem.getStock()) {
                existingItem.setQuantity(requestedQuantity);
            } else {
                throw new IllegalArgumentException("No puedes agregar más de este producto. Stock límite alcanzado.");
            }
        } else {
            // 3. Si es null, el producto no estaba en el carrito, lo buscamos en la BD y lo agregamos.
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado."));

            if (product.getStock() >= quantity) {
                CartItem newItem = new CartItem();
                newItem.setProductId(product.getIdProduct());
                newItem.setName(product.getName());
                newItem.setPrice(product.getPrice());
                newItem.setImage(product.getImage());
                newItem.setStock(product.getStock()); // Para que funcione tu th:max
                newItem.setQuantity(quantity);
                
                cart.add(newItem);
            } else {
                throw new IllegalArgumentException("Este producto no tiene stock disponible.");
            }
        }
    }

    public void increaseProduct(List<CartItem> cart, Integer productId) {
        CartItem existingItem = null;
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            if (existingItem.getQuantity() < existingItem.getStock()) {
                existingItem.setQuantity(existingItem.getQuantity() + 1);
            } else {
                throw new IllegalArgumentException("Has alcanzado el límite de stock disponible.");
            }
        } else {
            throw new IllegalArgumentException("El producto no está en el carrito.");
        }
    }


    public void decreaseProduct(List<CartItem> cart, Integer productId) {
        cart.removeIf(item -> {
            if (!item.getProductId().equals(productId)) {
                return false;
            }

            item.setQuantity(item.getQuantity() - 1);
            return item.getQuantity() <= 0;
        });
    }

    public void removeProduct(List<CartItem> cart, Integer productId) {
        cart.removeIf(item -> item.getProductId().equals(productId));
    }

    public BigDecimal calculateTotal(List<CartItem> cart) {
        return cart.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String resolveImage(String image) {
        if (image == null || image.isBlank()) {
            return DEFAULT_IMAGE;
        }

        return image;
    }
}
