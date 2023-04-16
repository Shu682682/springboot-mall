package com.shushu.springbootmall.controller;

import com.shushu.springbootmall.dto.UserRegisterRequest;
import com.shushu.springbootmall.model.User;
import com.shushu.springbootmall.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        //創建新帳號API, 返回值事responseEntitiy 方法名稱;rigister
        //@requestbody=接住前端傳過來的requestbody @valid 驗證Post請求的requestbody參數

        //實作新帳號功能
        Integer userId=userService.register(userRegisterRequest);
        //提供register方法(參數userrigser)為使用者在資料庫中創建比新的數據，成功後會返回資料哭生成的userId給我們
        //所以前面加入Integer userId去接住方法返回值

        User user =userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
        //預期userService 根據user傳進的id參數去查詢user資料
        //回傳response給前端 把創建出來的user數據放在body裡面回傳給前端

        //




    }
}
