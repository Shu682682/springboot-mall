package com.shushu.springbootmall.service.impl;

import com.shushu.springbootmall.dao.UserDao;
import com.shushu.springbootmall.dto.UserRegisterRequest;
import com.shushu.springbootmall.model.User;
import com.shushu.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {
    private final static Logger log= LoggerFactory.getLogger(UserServiceImpl.class);
    //創建log變數出來
    @Autowired
    private UserDao userDao; //把UserDao這個bean也輸入

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);//把userId當成參數傳回去
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        //一個email只能創建一個email出來假設已經被註冊要回傳400給前端表示資料有問題
        //查詢是否已經有資料存ㄗㄞˊ
        //檢查註冊的email
        User user=userDao.getUserByEamil(userRegisterRequest.getEmail());
        if(user!=null){//如果有資料就不會創造出新帳號不會執行return下面那行

            log.warn("該email{}已經被註冊", userRegisterRequest.getEmail());
            //log等級使用warn使用大括號的值就是後面參數的值
            //log字串裡面有很多對大括號 e.g.該email{}已經被{}註冊 但是後面要有兩個變數"Judy"
            //按照順序一個一個取得後面參數的值
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //創建帳號
        //提供getemail的方法透過前端惡值去茲料庫查巡對應的方法然後回傳並用User user去get返回的值
        return userDao.createUser(userRegisterRequest);
        //直接去call Userdao createuser的方法把userRegisterRequest傳進去
        //並且把userdao的返回值回傳進去


    }

}
