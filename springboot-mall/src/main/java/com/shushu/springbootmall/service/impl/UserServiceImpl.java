package com.shushu.springbootmall.service.impl;

import com.shushu.springbootmall.dao.UserDao;
import com.shushu.springbootmall.dto.UserRegisterRequest;
import com.shushu.springbootmall.model.User;
import com.shushu.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao; //把UserDao這個bean也輸入

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);//把userId當成參數傳回去
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
        //直接去call Userdao createuser的方法把userRegisterRequest傳進去
        //並且把userdao的返回值回傳進去
    }

}
