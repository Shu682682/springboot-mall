package com.shushu.springbootmall.dao;

import com.shushu.springbootmall.dto.ProductRequest;
import com.shushu.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
}
