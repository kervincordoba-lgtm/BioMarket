package com.biomarket.demo.controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.biomarket.demo.model.Category;
import com.biomarket.demo.model.Product;
import com.biomarket.demo.service.CategoryService;
import com.biomarket.demo.service.ProductService;

@Controller
@RequestMapping("/")
public class IndexController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public IndexController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping({"", "index.html"})
     public String index(Model model) {
        List<Product> products = productService.findAllWithStock();
        List<Category> categories = categoryService.findAll();
        Set<Integer> categoryIdsWithProducts = products.stream()
                .map(Product::getCategory)
                .filter(Objects::nonNull)
                .map(Category::getIdCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        model.addAttribute("product", new Product());
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryIdsWithProducts", categoryIdsWithProducts);
        return "index" ;
    
     }
    
    @GetMapping({"shop", "shop.html"})
    public String shop(@RequestParam(required = false) String productName,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) String sort,
            Model model) {
        model.addAttribute("products", productService.searchWithStock(productName, categoryId, sort));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("productName", productName);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("sort", sort);
        return "shop";
    }
}
