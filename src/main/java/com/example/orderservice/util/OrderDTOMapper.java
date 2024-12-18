package com.example.orderservice.util;

import com.example.orderservice.model.Order;
import model.OrderDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrderDTOMapper implements Function<Order, OrderDTO> {
    @Override
    public OrderDTO apply(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getTotal()
        );
    }
}
