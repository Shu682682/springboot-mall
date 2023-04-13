package com.shushu.springbootmall.service;

import com.shushu.springbootmall.constant.ProductCategory;
import com.shushu.springbootmall.dto.ProductRequest;
import com.shushu.springbootmall.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProducts(ProductCategory category, String search);
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);

    //update product沒有返回值寫void
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProductById(Integer productId);
}
