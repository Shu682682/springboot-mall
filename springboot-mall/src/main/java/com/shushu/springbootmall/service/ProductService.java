package com.shushu.springbootmall.service;

import com.shushu.springbootmall.dto.ProductRequest;
import com.shushu.springbootmall.model.Product;

public interface ProductService {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);

    //update product沒有返回值寫void
    void updateProduct(Integer productId, ProductRequest productRequest);
}
