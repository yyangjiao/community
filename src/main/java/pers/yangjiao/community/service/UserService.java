package pers.yangjiao.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.yangjiao.community.dao.UserMapper;
import pers.yangjiao.community.entity.User;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    public User findUserById(int id){
        return userMapper.selectById(id);
    }
}
