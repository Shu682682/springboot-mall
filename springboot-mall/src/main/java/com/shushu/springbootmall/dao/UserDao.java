package com.shushu.springbootmall.dao;

import com.shushu.springbootmall.dto.UserRegisterRequest;
import com.shushu.springbootmall.model.User;

public interface UserDao {
    User getUserById(Integer userId);
    User getUserByEamil(String email);
    Integer createUser(UserRegisterRequest userRegisterRequest);
}
