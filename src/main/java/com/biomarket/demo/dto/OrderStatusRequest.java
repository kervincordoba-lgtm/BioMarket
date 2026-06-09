package com.biomarket.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderStatusRequest(
        @NotBlank(message = "El estado del pedido es obligatorio.")
        String status) {
}
