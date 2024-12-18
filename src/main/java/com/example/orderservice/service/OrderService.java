package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import com.example.orderservice.persistence.OrderRepo;
import com.example.orderservice.util.OrderDTOMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import model.OrderDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;

    private final OrderDTOMapper mapper;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        var savedOrder = orderRepo.save(
                Order.builder()
                        .total(orderDTO.total())
                        .build()
        );
        return mapper.apply(savedOrder);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepo.findAll().stream().map(mapper).toList();
    }

    public OrderDTO getOrderById(String id) {
        return orderRepo.findById(id).map(mapper).orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    public void deleteOrderById(String id) {
        var order = orderRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        orderRepo.delete(order);
    }
}