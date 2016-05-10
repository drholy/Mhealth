package com.mhealth.repository;

import com.mhealth.common.base.BaseDao;
import com.mhealth.model.Comment;
import com.mhealth.model.User;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by pengt on 2016.4.11.0011.
 */
@Repository("userDao")
public class UserDao extends BaseDao {

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    public String insertUser(User user) {
        mongoTemplate.insert(user);
        return user.getLoginName();
    }

    /**
     * 返回登录名是否存在
     *
     * @param loginName
     * @return
     */
    public boolean checkUserByLn(String loginName) {
        return mongoTemplate.count(new Query(Criteria.where("loginName").is(loginName)), User.class) == 0 ? true : false;
    }

    /**
     * 根据登录名返回用户
     *
     * @param loginName
     * @return
     */
    public User getUser(String loginName) {
        return mongoTemplate.findOne(new Query(Criteria.where("loginName").is(loginName)), User.class);
    }

    /**
     * 根据id返回用户
     *
     * @param id
     * @return
     */
    public User getUserById(String id) {
        return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), User.class);
    }

    /**
     * 账户激活
     *
     * @param user
     * @return
     */
    public boolean active(User user) {
        WriteResult wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(user.getId()))),
                new Update().set("username", user.getUsername()).set("sex", user.getSex()).set("birthday", user.getBirthday())
                        .set("bloodType", user.getBloodType()).set("mobilePhone", user.getMobilePhone())
                        .set("email", user.getEmail()).set("active", user.getActive()).set("status", user.getStatus()), User.class);
        return wr.getN() == 1;
    }

    /**
     * 资料修改
     *
     * @param user
     * @return
     */
    public boolean modify(User user) {
        WriteResult wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(user.getId()))),
                new Update().set("username", user.getUsername()).set("sex", user.getSex()).set("birthday", user.getBirthday())
                        .set("bloodType", user.getBloodType()).set("mobilePhone", user.getMobilePhone())
                        .set("email", user.getEmail()), User.class);
        return wr.getN() == 1;
    }

    /**
     * 密码修改
     *
     * @param user
     * @return
     */
    public boolean changePasswd(User user) {
        WriteResult wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(user.getId()))),
                new Update().set("password", user.getPassword()), User.class);
        return wr.getN() == 1;
    }

    /**
     * 添加评论
     *
     * @param userId
     * @param comment
     * @return
     */
    public boolean addComment(String userId, Comment comment) {
        WriteResult wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(userId))), new Update().push("comments", comment), User.class);
        return wr.getN() == 1;
    }
}
