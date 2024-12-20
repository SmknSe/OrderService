package com.example.orderservice;

import com.example.orderservice.model.Order;
import com.example.orderservice.persistence.OrderRepo;
import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import model.OrderDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testCreateOrder() {
        UUID id = UUID.randomUUID();
        OrderDTO orderDTO = new OrderDTO(id, BigDecimal.valueOf(100.0));

        Order savedOrder = new Order();
        savedOrder.setTotal(BigDecimal.valueOf(100.0));

        when(orderRepo.save(any(Order.class))).thenReturn(savedOrder);
        when(mapper.convertValue(savedOrder, OrderDTO.class)).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertEquals(orderDTO, result);
        verify(orderRepo, times(1)).save(any(Order.class));
        verify(mapper, times(1)).convertValue(savedOrder, OrderDTO.class);
    }

    @Test
    public void testGetAllOrders() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        order.setTotal(BigDecimal.valueOf(100.0));

        OrderDTO orderDTO = new OrderDTO(id, BigDecimal.valueOf(100.0));

        when(orderRepo.findAll()).thenReturn(Collections.singletonList(order));
        when(mapper.convertValue(order, OrderDTO.class)).thenReturn(orderDTO);

        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals(orderDTO, result.getFirst());
        verify(orderRepo, times(1)).findAll();
        verify(mapper, times(1)).convertValue(order, OrderDTO.class);
    }

    @Test
    public void testGetOrderById() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        order.setTotal(BigDecimal.valueOf(100.0));

        OrderDTO orderDTO = new OrderDTO(id, BigDecimal.valueOf(100.0));

        when(orderRepo.findById(id.toString())).thenReturn(Optional.of(order));
        when(mapper.convertValue(order, OrderDTO.class)).thenReturn(orderDTO);

        OrderDTO result = orderService.getOrderById(id.toString());

        assertEquals(orderDTO, result);
        verify(orderRepo, times(1)).findById(id.toString());
        verify(mapper, times(1)).convertValue(order, OrderDTO.class);
    }

    @Test
    public void testGetOrderByIdNotFound() {
        UUID id = UUID.randomUUID();

        when(orderRepo.findById(id.toString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(id.toString()));
        verify(orderRepo, times(1)).findById(id.toString());
    }

    @Test
    public void testDeleteOrderById() {
        String id = "1";
        Order order = new Order();
        order.setTotal(BigDecimal.valueOf(100.0));

        when(orderRepo.findById(id)).thenReturn(Optional.of(order));

        orderService.deleteOrderById(id);

        verify(orderRepo, times(1)).findById(id);
        verify(orderRepo, times(1)).delete(order);
    }
}