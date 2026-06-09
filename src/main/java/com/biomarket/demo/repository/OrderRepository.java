package com.biomarket.demo.repository;

import com.biomarket.demo.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @EntityGraph(attributePaths = { "client", "details", "details.product" })
    @Query("select distinct o from Order o")
    List<Order> findAllWithDetails();

    @EntityGraph(attributePaths = { "client", "details", "details.product" })
    @Query("select o from Order o where o.idOrder = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Integer id);

    @Modifying
    @Query("update Order o set o.status = :status where o.idOrder = :id")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
}
