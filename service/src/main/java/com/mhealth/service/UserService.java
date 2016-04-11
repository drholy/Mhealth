package com.mhealth.service;

import com.mhealth.model.User;
import com.mhealth.repository.UserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by pengt on 2016.4.6.0006.
 */
@Service("userService")
public class UserService {

    @Resource(name="userDao")
    UserDao userDao;

    public String insertUser(User user) {
        return userDao.insertUser(user);
    }

    public boolean checkUserByLn(String loginName){
        return userDao.checkUserByLn(loginName);
    }
}
