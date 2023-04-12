package com.shushu.springbootmall.controller;
import com.shushu.springbootmall.dto.ProductRequest;
import com.shushu.springbootmall.model.Product;
import com.shushu.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //新增商品功能 根據Restful格式新增商品要用post功能
    @PostMapping("/products")
    //定義post方法的實作參數()創建一個新的class專門負責去接前端傳過來的json參數
    //productrequest=參數+@Requestbody(接住前端返回的json參數）+Valid(有+Notnull註解一定要加才會生效)
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        //實作新增商品功能
        //預期product servcie 會提供createproduct的方法(參數＝productReqeust)預期這個方法會去資料庫中創建商品出來
        //返回資料庫中生成的productID
       Integer productId= productService.createProduct(productRequest);
       //從資料庫取得商品數據
       Product product =productService.getProductById(productId);
       //回傳response給前段 created=201（創建的商品數據放在(body)裡面
       return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
}
