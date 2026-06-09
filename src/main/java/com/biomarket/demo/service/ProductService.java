package com.biomarket.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.biomarket.demo.model.Product;
import com.biomarket.demo.repository.ProductRepository;

@Service
public class ProductService {

    private static final String IMAGE_DIRECTORY = "src/main/resources/static/img";

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findAllWithStock() {
        return productRepository.findByStockGreaterThan(0);
    }

    public List<Product> findAllByCategory(Integer id) {
        return productRepository.findByCategory_IdCategory(id);
    }
    public List<Product> findAllByCategoryWithStock(Integer id) {
        return productRepository.findByCategory_IdCategoryWithStock(id);
    }

    public List<Product> search(String productName, Integer categoryId) {
        return search(productName, categoryId, null);
    }

    public List<Product> search(String productName, Integer categoryId, String sort) {
        boolean hasProductName = productName != null && !productName.isBlank();
        boolean hasCategory = categoryId != null;
        List<Product> products;

        if (hasProductName && hasCategory) {
            products = productRepository.findByNameContainingIgnoreCaseAndCategory_IdCategory(productName.trim(),
                    categoryId);
        } else if (hasProductName) {
            products = productRepository.findByNameContainingIgnoreCase(productName.trim());
        } else if (hasCategory) {
            products = productRepository.findByCategory_IdCategory(categoryId);
        } else {
            products = findAll();
        }

        return sortProducts(products, sort);
    }

    public List<Product> searchWithStock(String productName, Integer categoryId) {
        return searchWithStock(productName, categoryId, null);
    }

    public List<Product> searchWithStock(String productName, Integer categoryId, String sort) {
        boolean hasProductName = productName != null && !productName.isBlank();
        boolean hasCategory = categoryId != null;
        List<Product> products;

        if (hasProductName && hasCategory) {
            products = productRepository.findByNameContainingIgnoreCaseAndCategory_IdCategoryWhithStock(productName.trim(),
                    categoryId);
        } else if (hasProductName) {
            products = productRepository.findByNameContainingIgnoreCaseWithStock(productName.trim());
        } else if (hasCategory) {
            products = productRepository.findByCategory_IdCategoryWithStock(categoryId);
        } else {
            products = findAllWithStock();
        }

        return sortProducts(products, sort);
    }

    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) { // Redundante
        return productRepository.save(product);
    }

    public Product save(Product product, MultipartFile imageFile) throws IOException {
        String imageName = saveImage(imageFile);

        if (imageName != null) {
            product.setImage(imageName);
        }

        return productRepository.save(product);
    }

    public Product update(Integer id, Product productData) {
        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            updateProductData(product, productData);

            return productRepository.save(product);
        }

        return null;
    }

    public Product update(Integer id, Product productData, MultipartFile imageFile) throws IOException {
        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            updateProductData(product, productData);

            String imageName = saveImage(imageFile);

            if (imageName != null) {
                product.setImage(imageName);
            }

            return productRepository.save(product);
        }

        return null;
    }

    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    private void updateProductData(Product product, Product productData) {
        product.setName(productData.getName());
        product.setDescription(productData.getDescription());
        product.setPrice(productData.getPrice());
        product.setStock(productData.getStock());
        product.setCategory(productData.getCategory());

        if (productData.getImage() != null && !productData.getImage().isBlank()) {
            product.setImage(productData.getImage());
        }
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        String originalFileName = imageFile.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            return null;
        }

        String cleanFileName = Paths.get(originalFileName)
                .getFileName()
                .toString()
                .replace(" ", "_");

        String fileName = System.currentTimeMillis() + "_" + cleanFileName;
        Path uploadPath = Paths.get(IMAGE_DIRECTORY);

        Files.createDirectories(uploadPath);
        Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    private List<Product> sortProducts(List<Product> products, String sort) {
        Comparator<Product> comparator = switch (sort == null ? "" : sort) {
            case "name" -> Comparator.comparing(Product::getName,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            case "category" -> Comparator.comparing(
                    product -> product.getCategory() != null ? product.getCategory().getName() : null,
                    Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            default -> null;
        };

        if (comparator == null) {
            return products;
        }

        return products.stream()
                .sorted(comparator)
                .toList();
    }
}
