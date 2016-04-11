package com.mhealth.repository;

import com.mhealth.common.base.BaseDao;
import com.mhealth.model.User;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by pengt on 2016.4.11.0011.
 */
@Repository("userDao")
public class UserDao extends BaseDao {

    public String insertUser(User user) {
        mongoTemplate.insert(user);
        return user.getId();
    }

    public boolean checkUserByLn(String loginName) {
        return mongoTemplate.count(new Query(Criteria.where("loginName").is(loginName)), User.class) == 0 ? true : false;
    }
}
