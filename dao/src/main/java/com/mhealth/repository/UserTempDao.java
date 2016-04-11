package com.mhealth.repository;

import com.mhealth.model.UserTemp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by pengt on 2016.4.6.0006.
 */
@Repository("userTempDao")
public class UserTempDao {
    @Resource(name = "jdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public boolean insertUser(UserTemp userTemp) {
        String sql = "insert into user values(?,?,?)";
        int i = jdbcTemplate.update(sql, userTemp.getId(), userTemp.getName(), userTemp.getPassword());
        return i == 0 ? false : true;
    }

    public List<UserTemp> queryAllUsers() {
        String sql = "select * from user";
        List<UserTemp> list = jdbcTemplate.queryForList(sql, UserTemp.class);
        return list;
    }
}
