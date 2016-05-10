package com.mhealth.repository;

import com.mhealth.common.base.BaseDao;
import com.mhealth.common.entity.QuickPager;
import com.mhealth.model.Doctor;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by pengt on 2016.5.9.0009.
 */
@Repository("doctorDao")
public class DoctorDao extends BaseDao {

    /**
     * 医生注册
     *
     * @param doctor
     */
    public String insertDoc(Doctor doctor) {
        mongoTemplate.insert(doctor);
        return doctor.getLoginName();
    }

    /**
     * 分页返回所有医生
     *
     * @param quickPager
     */
    public void getAllDoc(QuickPager<Doctor> quickPager) {
        Query query = new Query();
        long count = mongoTemplate.count(query, Doctor.class);
        quickPager.setTotalRows(Integer.parseInt(String.valueOf(count)));
        query.with(new Sort(Sort.Direction.DESC, "regTime")).with(new Sort(Sort.Direction.ASC, "userList"))
                .skip(quickPager.getBeginNum()).limit(quickPager.getPageSize());
        List<Doctor> records = mongoTemplate.find(query, Doctor.class);
        quickPager.setData(records);
    }

    /**
     * 检查loginName
     *
     * @param loginName
     * @return
     */
    public boolean checkLoginName(String loginName) {
        long count = mongoTemplate.count(new Query(Criteria.where("loginName").is(loginName)), Doctor.class);
        return count == 0;
    }

    /**
     * 根据loginName返回医生
     *
     * @param loginName
     * @return
     */
    public Doctor getDocByLogin(String loginName) {
        return mongoTemplate.findOne(new Query(Criteria.where("loginName").is(loginName)), Doctor.class);
    }

    /**
     * 根据医生id查询医生
     *
     * @param id
     * @return
     */
    public Doctor getDocById(String id) {
        return mongoTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))), Doctor.class);
    }

    /**
     * 选择医生
     *
     * @param doctorId
     * @param userMap
     * @return
     */
    public boolean chooseDoc(String doctorId, Map<String, Object> userMap) {
        WriteResult wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(doctorId)))
                , new Update().push("userList", userMap), Doctor.class);
        return wr.getN() == 1;
    }

    /**
     * 根据用户返回医生
     *
     * @param userId
     * @return
     */
    public Doctor getDocByUser(String userId) {
        return mongoTemplate.findOne(new Query(Criteria.where("userList.id").is(userId)), Doctor.class);
    }
}
