package com.lcwd.electronicStore.services;

import com.lcwd.electronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronicStore.dtos.OrderDto;
import com.lcwd.electronicStore.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(CreateOrderRequest orderDto);

    void removeOrder(String orderId);

    List<OrderDto> getOrderByUser(String userId);

    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);
}
