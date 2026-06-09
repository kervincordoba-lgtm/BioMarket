package com.biomarket.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "El nombre del producto es obligatorio.")
        String name,

        @NotBlank(message = "La descripcion del producto es obligatoria.")
        String description,

        @NotNull(message = "El precio es obligatorio.")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0.")
        BigDecimal price,

        @NotNull(message = "El stock es obligatorio.")
        @Min(value = 0, message = "El stock no puede ser negativo.")
        Integer stock,

        String image,

        @NotNull(message = "La categoria es obligatoria.")
        Integer categoryId) {
}
