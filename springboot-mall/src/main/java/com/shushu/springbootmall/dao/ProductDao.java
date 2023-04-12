package com.shushu.springbootmall.dao;

import com.shushu.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);
}
