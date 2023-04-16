package com.shushu.springbootmall.rowmapper;

import com.shushu.springbootmall.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {//記得imple Row要選javaspirng not tree

    @Override//Rowmapper將資料庫結果去轉換成userobject
    public User mapRow(ResultSet rs, int i) throws SQLException {
        User user =new User();
    user.setUserId(rs.getInt("user_id"));
    user.setEmail(rs.getString("email"));
    user.setPassword(rs.getString("password"));
    user.setCreatedDate(rs.getTimestamp("created_date"));
    user.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
        return user;
    }
}
