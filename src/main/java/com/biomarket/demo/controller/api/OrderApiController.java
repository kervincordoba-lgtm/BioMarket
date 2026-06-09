package com.biomarket.demo.controller.api;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.biomarket.demo.model.Order;
import com.biomarket.demo.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Pedidos", description = "Consultas REST de pedidos y actualizacion de estados")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "Listar pedidos", description = "Lista pedidos registrados con filtros opcionales por estado, email y rango de fechas.")
    public List<Order> listOrders(
            @Parameter(description = "Estado exacto del pedido")
            @RequestParam(required = false) String status,
            @Parameter(description = "Texto contenido en el email del cliente")
            @RequestParam(required = false) String email,
            @Parameter(description = "Fecha inicial del filtro")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "Fecha final del filtro")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return orderService.findAll()
                .stream()
                .filter(order -> matchesStatus(order, status))
                .filter(order -> matchesEmail(order, email))
                .filter(order -> matchesDateRange(order, fromDate, toDate))
                .sorted(Comparator.comparing(Order::getOrderDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .reversed())
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar pedido", description = "Obtiene un pedido con cliente y detalles.")
    public Order getOrder(@PathVariable Integer id) {
        return orderService.findByIdWithDetails(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado."));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado del pedido", description = "Cambia el estado administrativo de un pedido.")
    public Order updateStatus(@PathVariable Integer id, @RequestBody Order request) {
        if (request.getStatus() == null || request.getStatus().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado del pedido es obligatorio.");
        }

        boolean updated = orderService.updateStatus(id, request.getStatus());

        if (!updated) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado.");
        }

        return getOrder(id);
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
}
