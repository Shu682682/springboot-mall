package com.shushu.springbootmall.dao.impl;

import com.shushu.springbootmall.dao.ProductDao;
import com.shushu.springbootmall.model.Product;
import com.shushu.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

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
}
