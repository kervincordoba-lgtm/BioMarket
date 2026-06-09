package com.biomarket.demo.controller;

import com.biomarket.demo.model.Order;
import com.biomarket.demo.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/dashboard/orders")
public class OrderDashboardController {

    private static final List<String> ORDER_STATUSES = List.of(
            "Pendiente - Contra entrega",
            "Pendiente - Transferencia",
            "En preparacion",
            "Enviado",
            "Entregado",
            "Cancelado");

    private final OrderService orderService;

    public OrderDashboardController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String index(@RequestParam(required = false) String status,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model) {
        List<Order> allOrders = orderService.findAll().stream()
                .sorted(Comparator.comparing(Order::getOrderDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .reversed())
                .toList();

        List<Order> orders = allOrders.stream()
                .filter(order -> matchesStatus(order, status))
                .filter(order -> matchesEmail(order, email))
                .filter(order -> matchesDateRange(order, fromDate, toDate))
                .toList();

        model.addAttribute("orders", orders);
        model.addAttribute("statuses", ORDER_STATUSES);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("email", email);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("totalOrders", allOrders.size());
        model.addAttribute("pendingOrders", countPendingOrders(allOrders));
        model.addAttribute("salesTotal", calculateSalesTotal(allOrders));

        return "dashboard-orders";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Integer id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        boolean updated = orderService.updateStatus(id, status);

        if (!updated) {
            redirectAttributes.addFlashAttribute("error", "Pedido no encontrado.");
            return "redirect:/dashboard/orders";
        }

        redirectAttributes.addFlashAttribute("message", "Estado del pedido actualizado.");

        return "redirect:/dashboard/orders";
    }

    private boolean matchesStatus(Order order, String status) {
        return status == null || status.isBlank() || status.equals(order.getStatus());
    }

    private boolean matchesEmail(Order order, String email) {
        if (email == null || email.isBlank()) {
            return true;
        }

        return order.getClient() != null
                && order.getClient().getEmail() != null
                && order.getClient().getEmail().toLowerCase().contains(email.trim().toLowerCase());
    }

    private boolean matchesDateRange(Order order, LocalDate fromDate, LocalDate toDate) {
        if (order.getOrderDate() == null) {
            return fromDate == null && toDate == null;
        }

        LocalDate orderDate = order.getOrderDate().toLocalDate();

        return (fromDate == null || !orderDate.isBefore(fromDate))
                && (toDate == null || !orderDate.isAfter(toDate));
    }

    private long countPendingOrders(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() != null && order.getStatus().startsWith("Pendiente"))
                .count();
    }

    private BigDecimal calculateSalesTotal(List<Order> orders) {
        return orders.stream()
                .map(Order::getTotal)
                .filter(total -> total != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
