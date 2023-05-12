package com.shushu.springbootmall.dao;

import com.shushu.springbootmall.dto.OrderQueryParams;
import com.shushu.springbootmall.model.Order;
import com.shushu.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {
    Integer countOrder(OrderQueryParams orderQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Order getOrderById(Integer orderId);
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
    //查訊訂單中所有品項因為購賣很多商品所以返回值是list
    Integer createOrder(Integer userId, Integer totalAmount);
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
