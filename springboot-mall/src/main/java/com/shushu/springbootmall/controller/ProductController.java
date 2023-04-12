package com.shushu.springbootmall.controller;
import com.shushu.springbootmall.model.Product;
import com.shushu.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/products/{productId}")//取得某個商品的數據
    public ResponseEntity<Product>getProduct(@PathVariable Integer productId){
        //這個productId的值是url路徑傳進來
        //前端請求url路徑透過get去資料庫中查詢數據
      Product product=productService.getProductById(productId);
      if(product!=null){//如果查到的值不是null就回傳http狀態碼200=OK
          //body.(product)就是查到的值返回給前端
          return ResponseEntity.status(HttpStatus.OK).body(product);
      }else{//不熟狀態碼看4-14課程
          return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
    }
}
