package com.biomarket.demo.dto;

import com.biomarket.demo.model.Client;
import com.biomarket.demo.model.Order;
import com.biomarket.demo.model.OrderDetail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Integer idOrder,
        LocalDateTime orderDate,
        BigDecimal total,
        String status,
        ClientSummary client,
        List<OrderDetailSummary> details) {

    public static OrderResponse from(Order order) {
        ClientSummary client = order.getClient() != null ? ClientSummary.from(order.getClient()) : null;
        List<OrderDetailSummary> details = order.getDetails().stream()
                .map(OrderDetailSummary::from)
                .toList();

        return new OrderResponse(
                order.getIdOrder(),
                order.getOrderDate(),
                order.getTotal(),
                order.getStatus(),
                client,
                details);
    }

    public record ClientSummary(
            Integer idClient,
            String firstName,
            String lastName,
            String email,
            String phone,
            String address) {

        public static ClientSummary from(Client client) {
            return new ClientSummary(
                    client.getIdClient(),
                    client.getFirstName(),
                    client.getLastName(),
                    client.getEmail(),
                    client.getPhone(),
                    client.getAddress());
        }
    }

    public record OrderDetailSummary(
            Integer idDetail,
            Integer productId,
            String productName,
            Integer quantity,
            BigDecimal subtotal) {

        public static OrderDetailSummary from(OrderDetail detail) {
            Integer productId = detail.getProduct() != null ? detail.getProduct().getIdProduct() : null;
            String productName = detail.getProduct() != null ? detail.getProduct().getName() : null;

            return new OrderDetailSummary(
                    detail.getIdDetail(),
                    productId,
                    productName,
                    detail.getQuantity(),
                    detail.getSubtotal());
        }
    }
}
