package com.shushu.springbootmall.controller;

import com.shushu.springbootmall.dto.CreateOrderRequest;
import com.shushu.springbootmall.dto.OrderQueryParams;
import com.shushu.springbootmall.model.Order;
import com.shushu.springbootmall.service.OrderService;
import com.shushu.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>>getOrders(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,//分頁功能
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){
        OrderQueryParams orderQueryParams=new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        //取得order list
        List<Order> orderList=orderService.getOrders(orderQueryParams);
        //取得order總數
        Integer count= orderService.countOrder(orderQueryParams);

        //分頁
        Page<Order>page =new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);
        return ResponseEntity.status(HttpStatus.OK).body(page);

    }

    @PostMapping("/users/{userId}/orders")//建立帳號已經存在下訂單功能才有意義/=的子集合的概念
    //在userId底下創建一個訂單出來，已有帳號訂單功能才有意義
    public ResponseEntity<?>createOrder(@PathVariable Integer userId,
                                        //()內是參數，接住url路徑返回的值(取得userId的值）
                                        @RequestBody @Valid CreateOrderRequest createOrderRequest){
                                        //Create要去接住前端的JSOn參數 valid=要去驗證post請求的reuqestbody參數
        Integer orderId=orderService.createOrder(userId,createOrderRequest);
        //預期orderServcie提供createOrder方法，方法參數()create order方法返回一個orderId給我們 orderId=資料庫創建的
        Order order=orderService.getOrderById(orderId);
        //根據orderId查詢資料數據出來
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
        //responsebody(回傳order=嘗試去資料庫中取得訂單的數據)給前端

    }
}
