package com.jwt.mapper;

import com.jwt.domain.model.UserInfo;
import com.jwt.domain.request.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    int insertUser(User user);

    List<User> getUserList();

    User getFindByEmail(String email);

    UserInfo getUserInfo(String email);
}
