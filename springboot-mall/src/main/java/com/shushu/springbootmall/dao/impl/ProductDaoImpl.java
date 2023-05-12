package com.shushu.springbootmall.dao.impl;

import com.shushu.springbootmall.dao.ProductDao;
import com.shushu.springbootmall.dto.ProductQueryParams;
import com.shushu.springbootmall.dto.ProductRequest;
import com.shushu.springbootmall.model.Product;
import com.shushu.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {
    //注入nameparatmeterJDBC這個Bean
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql= "SELECT count(*) FROM product WHERE 1=1";
        //實作查詢條件 根據前端傳過來的查詢條件的值去拼接sql語句
        Map<String, Object> map =new HashMap<>();//建立空map
        //查詢條件的sql語句
        sql=addFilteringSql(sql, map, productQueryParams);
        Integer total=namedParameterJdbcTemplate.queryForObject(sql, map,Integer.class);
        ///queryforobject用在取得count值
        //Integer.class表示要將count值轉換成Integer類型
        //Integer total 可以接著countsql語句的查詢結果
        return total;
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {//
        //SELECt sql無參數 全部先查詢出來
        String sql="SELECT product_id, product_name, category, image_url, price, stock, " +
                "description, created_date, last_modified_date " +
                "FROM product WHERE 1=1";//where1=1：想讓下面查詢條件可自由拼接sql語句
                //組合下面的查詢條件可用最簡單的方式去拼接and雨具在下面
                //spring dataJPA會自動使用不需要添加
        Map<String, Object> map =new HashMap<>();//建立空map
        //查詢條件的sql語句
        sql=addFilteringSql(sql, map, productQueryParams);
        //分頁的sql語句
        sql=sql+" ORDER BY "+productQueryParams.getOrderBy()+" "+productQueryParams.getSort();
        //會在ＷＨＥＲＥ語句後面拼接sql語法 並根據orderBY指定的欄位進行升序
        //只能使用字串拼接sql語句不能用變數去使用e.g. :search (技術限制）
        //不用檢查參數是否為null 因為有寫Defaultvalue設定
        // 拼接sql語句一定要記得前後留空白鍵e.g. " ORDER BY "

        sql=sql +" LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());//把前端傳過來limit的值加入map
        map.put("offset", productQueryParams.getOffset());//沒＋null判斷式因為有在controller曾設定預設值
        // 即使前端沒傳參數兩個參數也不會是null

        List<Product> productList=namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        //執行玩sql取得資料數據後回傳出去
        return productList;
    }

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
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        //建立判斷式如果size>0才取得第一個值返回，若為空則反回null
        if(productList.size()>0){
            return productList.get(0);
        }else{  return null;
        }

    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        //insert product的sql去設定每個欄位的值 把數據塞到資料庫
        String sql="INSERT INTO product(product_name, category, image_url, price, stock, " +
                "description, created_date, last_modified_date) " +
                "VALUES(:productName, :category, :imageUrl, :price, :stock, :description,"+
                ":createdDate, :lastModifiedDate)";
        //把前端前回的productreqeust參數一個一個加入map
        Map<String, Object> map =new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        //new date紀錄當下時間把當下時間當成商品創建跟最後加入的時間
        Date now =new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);
        //keyholder儲存資料庫自動生成的productId
        KeyHolder keyHolder=new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map),keyHolder);
        //將productID回傳出去(可參考第五章）
        int productId= keyHolder.getKey().intValue();
        return productId;

    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql="UPDATE product SET product_name= :productName, category= :category, image_url= :imageUrl, " +
                "price= :price, stock= :stock, description= :description, " +
                "last_modified_date= :lastModifiedDate" +
                " WHERE product_id= :productId";
        //sql裡面的值是前端傳回來的更新值,有數據改動要記得更新最後修改時間的值Lastmodifieddate
        Map<String, Object> map =new HashMap<>();
        //建立map把參數加進來
        map.put("productId",productId);
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        map.put("lastModifiedDate", new Date());
        namedParameterJdbcTemplate.update(sql, map);
        //執行這條sql修改商品的數據
    }

    @Override
    public void updateStock(Integer productId, Integer stock) {
        String sql ="UPDATE product SET stock = :stock, last_modified_date = :lastModifiedDate" +
                " WHERE product_id =:productId ";//更新stock 跟lastmodifiedDate
        Map<String, Object> map =new HashMap<>();
        map.put("productId",productId);
        map.put("stock", stock);
        map.put("lastModifiedDate", new Date());
        namedParameterJdbcTemplate.update(sql,map);

    }

    @Override//刪除商品
    public void deleteProductById(Integer productId) {
        String sql ="DELETE FROM product WHERE product_id= :productId";
        Map<String, Object> map =new HashMap<>();
        map.put("productId", productId);
        namedParameterJdbcTemplate.update(sql, map);//執行刪除
    }

    private String addFilteringSql(String sql, Map<String, Object> map, ProductQueryParams productQueryParams){
        //根據傳進來的參數去拼接sql語句
        //以後利用查詢條件去拼接sql語句只要使用addfiltersql的方法就可以達到效果
        if(productQueryParams.getCategory() != null){
            sql=sql+" AND category=:category";//一定要在and前面留一個空白鍵在拼接語句才不回跟前面查詢條件年再一起
            map.put("category", productQueryParams.getCategory().name());//把前端的category參數是enum類型所以要使用name方法
        }

        if(productQueryParams.getSearch() !=null){//只要 search 不是null 就會在後面去拼接語句 and 一定要留下空白建 拼接sql才不會出問題
            sql=sql+" AND product_name LIKE :search";
            map.put("search", "%" +productQueryParams.getSearch() +"%");//一定要寫在map的值裡面不能寫在sql語句
        }
        return sql;
    }


}
