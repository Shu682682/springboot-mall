package com.shushu.springbootmall.service;

import com.shushu.springbootmall.dto.CreateOrderRequest;
import com.shushu.springbootmall.dto.OrderQueryParams;
import com.shushu.springbootmall.model.Order;

import java.util.List;

public interface OrderService {
    Integer countOrder(OrderQueryParams orderQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Order getOrderById(Integer orderId);
    //定義createOrder的方法
    //返回值=Integer 方法名稱 (參數)
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
