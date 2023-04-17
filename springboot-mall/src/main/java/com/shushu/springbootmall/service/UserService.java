package com.shushu.springbootmall.service;

import com.shushu.springbootmall.dto.UserLoginRequest;
import com.shushu.springbootmall.dto.UserRegisterRequest;
import com.shushu.springbootmall.model.User;

public interface UserService {
    //定義register方法
    Integer register(UserRegisterRequest userRegisterRequest);
    //定義getUserbyId的方法
    User getUserById(Integer userId);

    User login(UserLoginRequest userLoginRequest);
        //返回user類型名稱login 參數（）
}
