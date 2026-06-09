package com.biomarket.demo.controller;

import com.biomarket.demo.model.Category;
import com.biomarket.demo.model.Product;
import com.biomarket.demo.service.CategoryService;
import com.biomarket.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/dashboard/products")
public class ProductDashboardController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductDashboardController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(@RequestParam(required = false) String productName,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String sort,
            Model model) {
        model.addAttribute("product", new Product());
        addDashboardAttributes(model, productName, categoryId, sort);
        return "dashboard-products";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            Model model,
            RedirectAttributes redirectAttributes) {

        Category category = categoryId != null ? categoryService.findById(categoryId).orElse(null) : null;

        if (category == null) {
            bindingResult.reject("product.category.required", "La categoria es obligatoria.");
        }

        if (imageFile == null || imageFile.isEmpty()) {
            bindingResult.reject("product.image.required", "La imagen del producto es obligatoria.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", firstValidationError(bindingResult));
            addDashboardAttributes(model, null, null, null);
            return "dashboard-products";
        }

        product.setCategory(category);

        try {
            productService.save(product, imageFile);
            redirectAttributes.addFlashAttribute("message", "Producto creado correctamente.");
        } catch (IOException exception) {
            redirectAttributes.addFlashAttribute("error", "No se pudo guardar la imagen.");
        }

        return "redirect:/dashboard/products";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
            @Valid @ModelAttribute Product product,
            BindingResult bindingResult,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {

        Category category = categoryId != null ? categoryService.findById(categoryId).orElse(null) : null;

        if (category == null) {
            redirectAttributes.addFlashAttribute("error", "Categoria no encontrada.");
            return "redirect:/dashboard/products";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", firstValidationError(bindingResult));
            return "redirect:/dashboard/products";
        }

        product.setCategory(category);

        try {
            Product updatedProduct = productService.update(id, product, imageFile);

            if (updatedProduct != null) {
                redirectAttributes.addFlashAttribute("message", "Producto actualizado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
            }
        } catch (IOException exception) {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar la imagen.");
        }

        return "redirect:/dashboard/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        boolean deleted = productService.deleteById(id);

        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "Producto eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar el producto porque ya esta asociado a uno o mas pedidos.");
        }

        return "redirect:/dashboard/products";
    }

    private void addDashboardAttributes(Model model, String productName, Integer categoryId, String sort) {
        model.addAttribute("products", productService.search(productName, categoryId, sort));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("productName", productName);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("sort", sort);
    }

    private String firstValidationError(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Revisa los datos.")
                .orElse("Revisa los datos.");
    }
}
