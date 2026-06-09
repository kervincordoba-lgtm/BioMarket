package com.biomarket.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biomarket.demo.model.CartItem;
import com.biomarket.demo.model.Client;
import com.biomarket.demo.model.Order;
import com.biomarket.demo.model.OrderDetail;
import com.biomarket.demo.model.Product;
import com.biomarket.demo.service.ClientService;
import com.biomarket.demo.service.CartService;
import com.biomarket.demo.service.OrderService;
import com.biomarket.demo.service.ProductService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;

@Controller
public class CheckoutController {
    private static final String CART_SESSION_KEY = "cart";

    private final CartService cartService;
    private final ClientService clientService;
    private final OrderService orderService;
    private final ProductService productService;

    public CheckoutController(CartService cartService, ClientService clientService, OrderService orderService,
            ProductService productService) {
        this.cartService = cartService;
        this.clientService = clientService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping({ "/checkout", "/checkout.html", "/chackout", "/chackout.html" })
    public String showCheckout(HttpSession session, Model model) {
        List<CartItem> cart = getCart(session);

        model.addAttribute("cart", cart);
        model.addAttribute("total", cartService.calculateTotal(cart));

        return "checkout";
    }

    @GetMapping("/order-confirmation/{id}")
    public String showOrderConfirmation(@PathVariable Integer id, Model model) {
        Order order = orderService.findByIdWithDetails(id).orElse(null);

        if (order == null) {
            return "error/404";
        }

        model.addAttribute("order", order);
        return "order-confirmation";
    }

    @PostMapping("/checkout")
    @Transactional
    public String placeOrder(@RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @RequestParam String address,
            @RequestParam(defaultValue = "Contra entrega") String paymentMethod,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        List<CartItem> cart = getCart(session);

        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tu carrito esta vacio.");
            return "redirect:/cart";
        }

        Client client = clientService.findByEmail(email)
                .orElseGet(Client::new);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhone(phone);
        client.setAddress(address);
        client = clientService.save(client);

        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Pendiente - " + paymentMethod);
        order.setTotal(cartService.calculateTotal(cart));

        for (CartItem item : cart) {
            Product product = productService.findById(item.getProductId()).orElse(null);

            if (product == null) {
                redirectAttributes.addFlashAttribute("error", "Un producto del carrito ya no existe.");
                return "redirect:/checkout";
            }

            if (product.getStock() < item.getQuantity()) {
                redirectAttributes.addFlashAttribute("error",
                        "No hay stock suficiente para " + product.getName() + ".");
                return "redirect:/checkout";
            }

            product.setStock(product.getStock() - item.getQuantity());
            productService.save(product);

            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setSubtotal(item.getSubtotal());
            order.getDetails().add(detail);
        }

        Order savedOrder = orderService.save(order);
        cart.clear();

        redirectAttributes.addFlashAttribute("message",
                "Pedido #" + savedOrder.getIdOrder() + " creado correctamente.");
        return "redirect:/order-confirmation/" + savedOrder.getIdOrder();
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
