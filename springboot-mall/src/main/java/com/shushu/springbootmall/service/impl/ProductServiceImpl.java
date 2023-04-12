package com.shushu.springbootmall.service.impl;

import com.shushu.springbootmall.dao.ProductDao;
import com.shushu.springbootmall.model.Product;
import com.shushu.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Override
    public Product getProductById(Integer productId) {
        //把參數放進去
        return productDao.getProductById(productId);
    }
}
