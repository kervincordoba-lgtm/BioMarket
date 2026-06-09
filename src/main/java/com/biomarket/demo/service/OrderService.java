package com.biomarket.demo.service;

import com.biomarket.demo.model.Order;
import com.biomarket.demo.model.OrderDetail;
import com.biomarket.demo.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAllWithDetails();
    }

    public Optional<Order> findById(Integer id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findByIdWithDetails(Integer id) {
        return orderRepository.findByIdWithDetails(id);
    }

    public Order save(Order order) {
        for (OrderDetail detail : order.getDetails()) {
            detail.setOrder(order);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public boolean updateStatus(Integer id, String status) {
        return orderRepository.updateStatus(id, status) > 0;
    }

    public void deleteById(Integer id) {
        orderRepository.deleteById(id);
    }
}
