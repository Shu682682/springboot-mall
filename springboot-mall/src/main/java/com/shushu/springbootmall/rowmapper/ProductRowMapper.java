package com.shushu.springbootmall.rowmapper;

import com.shushu.springbootmall.constant.ProductCategory;
import com.shushu.springbootmall.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    //要implement的事spring jdbc的mapper，加上泛行<>表示要轉換成product的java class

    @Override//實作maprow
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        //可從rs資料裡的參數去取得資料庫查詢的數據然後儲存在product的變數裡
        Product product=new Product();
        //因為productid是int類型所以要寫rs.getInt(寫sql語法)就可以取得productid的數據
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));
        //string 轉enum的用法！重要
        String categoryStr=rs.getString("category");
        //找出來的值會存放在category變數裡面
        ProductCategory category=ProductCategory.valueOf(categoryStr);
        product.setCategory(category);
        //也可寫成下面這種
        //product.setCategory(ProductCategory.valueOf(rs.getString("category")));

        product.setImageUrl(rs.getString("image_url"));
        product.setPrice(rs.getInt("price"));
        product.setStock(rs.getInt("stock"));
        product.setDescription(rs.getString("description"));
        product.setCreatedDate(rs.getTimestamp("created_date"));
        product.setLastModifiedDate(rs.getTimestamp("last_modified_date"));



        return product;
    }
}
