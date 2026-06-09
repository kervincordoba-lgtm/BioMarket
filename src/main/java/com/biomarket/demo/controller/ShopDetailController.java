package com.biomarket.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biomarket.demo.model.CartItem;
import com.biomarket.demo.model.Product;
import com.biomarket.demo.service.CartService;
import com.biomarket.demo.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopDetailController {
    
    private static final String CART_SESSION_KEY = "cart";
    
    private final ProductService productService;
    private final CartService cartService;

    public ShopDetailController(ProductService productService, CartService cartService){
        this.productService = productService;
        this.cartService = cartService;

    }

    @GetMapping("/shop-detail/{id}")
    public String showDetail(@PathVariable Integer id, HttpSession session, Model model) {
        Product product = productService.findById(id).orElse(null);

        if (product == null) {
            return "error/404";
        }

        model.addAttribute("product", product);
        model.addAttribute("cartTotal", cartService.calculateTotal(getCart(session)));
        model.addAttribute("products", productService.findAllByCategoryWithStock(product.getCategory().getIdCategory()));

        return "shop-detail";
    }
   

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }

        return cart;
    }
}
