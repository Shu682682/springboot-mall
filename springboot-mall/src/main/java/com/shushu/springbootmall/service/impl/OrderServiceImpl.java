package com.shushu.springbootmall.service.impl;

import com.shushu.springbootmall.dao.OrderDao;
import com.shushu.springbootmall.dao.ProductDao;
import com.shushu.springbootmall.dao.UserDao;
import com.shushu.springbootmall.dto.BuyItem;
import com.shushu.springbootmall.dto.CreateOrderRequest;
import com.shushu.springbootmall.dto.OrderQueryParams;
import com.shushu.springbootmall.model.Order;
import com.shushu.springbootmall.model.OrderItem;
import com.shushu.springbootmall.model.Product;
import com.shushu.springbootmall.model.User;
import com.shushu.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Component
public class OrderServiceImpl implements OrderService {
    private final static Logger log= LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList=orderDao.getOrders(orderQueryParams);
         for(Order order: orderList){
            List<OrderItem>orderItemList=orderDao.getOrderItemsByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);
        }
        return orderList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order =orderDao.getOrderById(orderId);//orderDao兩次分別從db中取得數據
        List<OrderItem>orderItemList=orderDao.getOrderItemsByOrderId(orderId);
        //分別取得ordertable跟orderItemtable中的數
        order.setOrderItemList(orderItemList);

        return order;
    }

    @Transactional
    //修改多張資料庫Table一定要在service曾加入transactional註解
    //萬一噴出exception會去復原已經執行過的資料庫操作
    //確保兩個資料庫會同時發生或同時不發生 all or never 萬一其中一個失敗可避免 確保數據一致
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        //檢查user是否存在
        User user =userDao.getUserById(userId);//去資料庫查詢user數據
        if(user ==null){
            log.warn("該 userId{} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //計算訂單總價錢
        int totalAmount=0;
        List<OrderItem> orderItemList=new ArrayList<>();
        //創建list裡面放orderitem
        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){
            //forloop使用者所購買的所有商品
            Product product= productDao.getProductById(buyItem.getProductId());
            //根據前端傳過來的值去資料庫查詢數據 用到productDaobean 上面要注入這個bean

            //檢查product是否存在庫存是否足夠
            if(product==null){
                log.warn("商品{}不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else if(product.getStock()<buyItem.getQuantity()){
                log.warn("商品{}庫存數量不足，無法購買。剩餘庫存{}，欲購買數量{}",
                        buyItem.getProductId(),product.getStock(),buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            //扣除倉品庫存
            productDao.updateStock(product.getProductId(),product.getStock() - buyItem.getQuantity());

            //計算總價錢
            int amount =buyItem.getQuantity()*product.getPrice();
            totalAmount=totalAmount+amount;
            //轉換Buytiem to Orderitem
            OrderItem orderItem=new OrderItem();
            //先new 一個再去設定裡裡面的值
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);//價錢是上面計算出來的
            orderItemList.add(orderItem);
            //把前端傳過來的buyitem資訊去轉換成orderitem
            // 計算每個商品價格
            // 把資料都加入上面的list當中  當成參數傳到dao幫我們在資料庫中插入數據

        }
        //創建訂單
        Integer orderId=orderDao.createOrder(userId, totalAmount);
        //Dao在ordertable創建一筆數據
        orderDao.createOrderItems(orderId, orderItemList);
        //()參數拜託DAO去插入數據
        //在call Dao在OrderItem也去創建數據
        //Service層會call兩次Dao
        return orderId;
    }
}
