package com.shushu.springbootmall.dao;

import com.shushu.springbootmall.constant.ProductCategory;
import com.shushu.springbootmall.dto.ProductRequest;
import com.shushu.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {
    List<Product> getProducts(ProductCategory category, String search);
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProductById(Integer productId);
}
