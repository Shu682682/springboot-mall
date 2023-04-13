package com.shushu.springbootmall.dao.impl;

import com.shushu.springbootmall.dao.ProductDao;
import com.shushu.springbootmall.dto.ProductRequest;
import com.shushu.springbootmall.model.Product;
import com.shushu.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {
    //注入nameparatmeterJDBC這個Bean
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Override
    public Product getProductById(Integer productId) {
        //先寫sql語法可以去sql console寫比較快
        String sql="SELECT product_id,product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date FROM product WHERE product_id = :productId";
        //建立map 將sql中的參數productId 注入進去
        Map<String, Object> map =new HashMap<>();
        map.put("productId", productId);
        //去執行select的sql語法去查詢商品數據
        // 第一個sql變數 2 map 3轉換數據的roadmapper(將sql中的數據轉換成java Object(先去新增rowmaaperpaackge
        //接住query方法的煩回值
        List<Product> prodcutList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        //建立判斷式如果size>0才取得第一個值返回，若為空則反回null
        if(prodcutList.size()>0){
            return prodcutList.get(0);
        }else{  return null;
        }

    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        //insert product的sql去設定每個欄位的值 把數據塞到資料庫
        String sql="INSERT INTO product(product_name, category, image_url, price, stock, " +
                "description, created_date, last_modified_date) " +
                "VALUES(:productName, :category, :imageUrl, :price, :stock, :description,"+
                ":createdDate, :lastModifiedDate)";
        //把前端前回的productreqeust參數一個一個加入map
        Map<String, Object> map =new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        //new date紀錄當下時間把當下時間當成商品創建跟最後加入的時間
        Date now =new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);
        //keyholder儲存資料庫自動生成的productId
        KeyHolder keyHolder=new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map),keyHolder);
        //將productID回傳出去(可參考第五章）
        int productId= keyHolder.getKey().intValue();
        return productId;

    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql="UPDATE product SET product_name= :productName, category= :category, image_url= :imageUrl, " +
                "price= :price, stock= :stock, description= :description, " +
                "last_modified_date= :lastModifiedDate" +
                " WHERE product_id= :productId";
        //sql裡面的值是前端傳回來的更新值,有數據改動要記得更新最後修改時間的值Lastmodifieddate
        Map<String, Object> map =new HashMap<>();
        //建立map把參數加進來
        map.put("productId",productId);
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        map.put("lastModifiedDate", new Date());
        namedParameterJdbcTemplate.update(sql, map);
        //執行這條sql修改商品的數據


    }


}
