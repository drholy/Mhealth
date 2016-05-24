package com.mhealth.service;

import com.mhealth.common.entity.QuickPager;
import com.mhealth.model.Comment;
import com.mhealth.model.Doctor;
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

    @Resource(name = "userDao")
    private UserDao userDao;

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    public String insertUser(User user) {
        return userDao.insertUser(user);
    }

    /**
     * 用户名是否存在
     *
     * @param loginName
     * @return
     */
    public boolean checkUserByLn(String loginName) {
        return userDao.checkUserByLn(loginName);
    }

    /**
     * 根据登录名返回user
     *
     * @param loginName
     * @return
     */
    public User getUser(String loginName) {
        return userDao.getUser(loginName);
    }

    /**
     * 根据id返回用户
     *
     * @param id
     * @return
     */
    public User getUserById(String id) {
        return userDao.getUserById(id);
    }

    /**
     * 账户激活
     *
     * @param user
     * @return
     */
    public boolean active(User user) {
        return userDao.active(user);
    }

    /**
     * 资料修改
     *
     * @param user
     * @return
     */
    public boolean modify(User user) {
        return userDao.modify(user);
    }

    /**
     * 密码修改
     *
     * @param user
     * @return
     */
    public boolean changePasswd(User user) {
        return userDao.changePasswd(user);
    }

    /**
     * 添加评论
     *
     * @param userId
     * @param comment
     * @return
     */
    @Deprecated
    public boolean addComment(String userId, Comment comment) {
        return userDao.addComment(userId, comment);
    }

    /**
     * 分页获取医生评论
     *
     * @param quickPager
     * @param userId
     */
    public void getComments(QuickPager<Comment> quickPager, String userId) {
        userDao.getComments(quickPager, userId);
    }

    /**
     * 分页返回合法医生
     *
     * @param quickPager
     */
    public void allAvaDoc(QuickPager<Doctor> quickPager) {
        userDao.allAvaDoc(quickPager);
    }
}
