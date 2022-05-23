package com.jwt.mapper;

import com.jwt.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    int insertUser(User user);
    List<User> getUserList();
    User getFindByEmail(String email);
}
