package com.biomarket.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biomarket.demo.model.CartItem;
import com.biomarket.demo.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    private static final String CART_SESSION_KEY = "cart";

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping({ "/cart", "/cart.html" })
    public String showCart(HttpSession session, Model model) {
        List<CartItem> cart = getCart(session);

        model.addAttribute("cart", cart);
        model.addAttribute("total", cartService.calculateTotal(cart));

        return "cart";
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Integer id,
            @RequestParam(defaultValue = "1") Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            cartService.addProduct(getCart(session), id, quantity);
            redirectAttributes.addFlashAttribute("message", "Producto agregado al carrito.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/increase/{id}")
    public String increaseProduct(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            cartService.increaseProduct(getCart(session), id);
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/decrease/{id}")
    public String decreaseProduct(@PathVariable Integer id, HttpSession session) {
        cartService.decreaseProduct(getCart(session), id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeProduct(@PathVariable Integer id, HttpSession session) {
        cartService.removeProduct(getCart(session), id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        getCart(session).clear();
        return "redirect:/cart";
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
