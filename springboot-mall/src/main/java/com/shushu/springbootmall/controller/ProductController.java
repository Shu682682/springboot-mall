package com.shushu.springbootmall.controller;

import com.shushu.springbootmall.constant.ProductCategory;
import com.shushu.springbootmall.dto.ProductQueryParams;
import com.shushu.springbootmall.dto.ProductRequest;
import com.shushu.springbootmall.model.Product;
import com.shushu.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/products")//因為商品很多所以要記得products要記得+s（必須)
    //查詢商品列表反回list(裡面裝的是product數據)名稱設為getPRODUCTS
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false)ProductCategory category,
            @RequestParam(required = false) String search//表示search不是必填=選填
            //required=false表示這是可選的參數4-7 (不帶參數也會回傳資料,就會回傳全部分類的資料)
            //使用商品分類篩選特定商品出來
            // RequestParam表示從url 請求取得的參數
            //前端可透過傳進來的category去指定要查看哪一類的商品
            //針對有預先定義的catoger的值可以用product cateogry enum springboot會自動幫忙轉換前端傳過來的category
            ){
        ProductQueryParams productQueryParams=new ProductQueryParams();//創建QP變數儲存前端傳送的參數
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);//把search值設定進去productqueryparams

        List<Product> productList=productService.getProducts(productQueryParams);
        //不管添加多少新的變數都可以固定傳遞productQueryParams
        //方法會回傳商品列表前面要加上List<Product>去接住getproducts返回的變數
        return ResponseEntity.status(HttpStatus.OK).body(productList);
        //回傳給前端 內容要寫productlist的值
        //補充；為何不用設計確認商品是否存在 因為RESTful的理念get/product資源還是存在

    }

    @GetMapping("/products/{productId}")//取得某個商品的數據 /代表階層跟子集合
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
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest){
        //接住url路徑傳回來的product值(Pathvariable) productId的值
        //Requestbody 接住前端返回的json參數要記得加上valid註解（for notnull才會生效）因為這是允許前端去修改的
        //Product request參數適合前端只能修product reuqest中變數的值

        //可以先檢查商品有沒有存在
        Product product =productService.getProductById(productId);
        if(product ==null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        //修改商品的數據
        productService.updateProduct(productId, productRequest);
        //第一個參數id:要背更新的商品 第二個：request表示商品修改過後的值
        //updateProduct不會特別返回什麼值
        Product updatedProduct= productService.getProductById((productId));
        //查詢更新後的商品數據 （）productId的值當成參數傳進去
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        //回傳給前端 http=200OK 更新過後的商品數據放在body(修改後的值);
    }
    @DeleteMapping("/products/{productId}")//將productid的值透過url路徑傳進來
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        //表示productId的值從url路徑傳過來的
        productService.deleteProductById(productId);//不用特別返回值
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        //表示數據被刪掉204把整個response回傳給前端
        //只要商品消失了就表示成功了所以不用確定本來有沒有值
        //前端只要確認他消失不見就好了就表示這個功能是成功的 所以不需要加404的判斷


    }

}
