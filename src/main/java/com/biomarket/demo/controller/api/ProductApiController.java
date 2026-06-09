package com.biomarket.demo.controller.api;

import com.biomarket.demo.dto.ProductRequest;
import com.biomarket.demo.dto.ProductResponse;
import com.biomarket.demo.model.Category;
import com.biomarket.demo.model.Product;
import com.biomarket.demo.service.CategoryService;
import com.biomarket.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Productos", description = "Operaciones REST para el CRUD de productos e inventario")
public class ProductApiController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductApiController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Lista productos con filtros opcionales por nombre, categoria y orden.")
    public List<ProductResponse> listProducts(
            @Parameter(description = "Texto para buscar por nombre del producto")
            @RequestParam(required = false) String productName,
            @Parameter(description = "Identificador de la categoria")
            @RequestParam(required = false) Integer categoryId,
            @Parameter(description = "Criterio de orden: name o category")
            @RequestParam(required = false) String sort) {
        return productService.search(productName, categoryId, sort).stream()
                .map(ProductResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar producto", description = "Obtiene un producto por su identificador.")
    public ProductResponse getProduct(@PathVariable Integer id) {
        return productService.findById(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado."));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear producto", description = "Registra un producto nuevo desde datos JSON.")
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = toProduct(request);
        return ProductResponse.from(productService.save(product));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos principales de un producto.")
    public ProductResponse updateProduct(@PathVariable Integer id, @Valid @RequestBody ProductRequest request) {
        Product productData = toProduct(request);
        Product updatedProduct = productService.update(id, productData);

        if (updatedProduct == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado.");
        }

        return ProductResponse.from(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su identificador.")
    public void deleteProduct(@PathVariable Integer id) {
        if (productService.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado.");
        }

        productService.deleteById(id);
    }

    private Product toProduct(ProductRequest request) {
        Category category = categoryService.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria no encontrada."));

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setImage(request.image());
        product.setCategory(category);
        return product;
    }
}
