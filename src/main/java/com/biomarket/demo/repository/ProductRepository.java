package com.biomarket.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biomarket.demo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.category WHERE p.stock > :stock")
    List<Product> findByStockGreaterThan(@Param("stock") Integer stock);

    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.category WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("productName") String productName);

    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.category WHERE p.category.idCategory = :categoryId")
    List<Product> findByCategory_IdCategory(@Param("categoryId") Integer categoryId);

    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.category WHERE p.category.idCategory = :categoryId AND p.stock > 0")
    List<Product> findByCategory_IdCategoryWithStock(@Param("categoryId") Integer categoryId);

    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.category WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) AND p.category.idCategory = :categoryId")
    List<Product> findByNameContainingIgnoreCaseAndCategory_IdCategory(@Param("productName") String productName, @Param("categoryId") Integer categoryId);

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.idProduct = :id")
    Optional<Product> findById(@Param("id") Integer id);


    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.category WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) AND p.category.idCategory = :categoryId AND p.stock>0")
    List<Product> findByNameContainingIgnoreCaseAndCategory_IdCategoryWhithStock(@Param("productName") String productName, @Param("categoryId") Integer categoryId);

    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.category WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) AND p.stock>0")
    List<Product> findByNameContainingIgnoreCaseWithStock(@Param("productName") String productName);

    
}
