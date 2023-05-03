package com.shushu.springbootmall.rowmapper;

import com.shushu.springbootmall.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper<OrderItem> {
    @Override
    public OrderItem mapRow(ResultSet rs, int i) throws SQLException {
        OrderItem orderItem=new OrderItem();//orderItem本來就有的欄位
        orderItem.setOrderItemId(rs.getInt("order_item_id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setProductId(rs.getInt("product_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setAmount(rs.getInt("amount"));
        //下面擴充orderItem
        orderItem.setProductName(rs.getString("product_Name"));
        orderItem.setImageUrl(rs.getString("image_url"));
        return orderItem;
    }
}
