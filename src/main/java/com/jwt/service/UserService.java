package com.jwt.service;

import com.jwt.mapper.UserMapper;
import com.jwt.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int createUser(User user){
        return userMapper.insertUser(user);
    }

    public User getFindByEmail(String email){
        System.out.println("email"+email);
        return userMapper.getFindByEmail(email);
    }
}
