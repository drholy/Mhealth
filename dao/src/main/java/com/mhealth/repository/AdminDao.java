package com.mhealth.repository;

import com.mhealth.common.base.BaseDao;
import com.mhealth.common.entity.QuickPager;
import com.mhealth.model.Admin;
import com.mhealth.model.Doctor;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.List;

/**
 * Created by pengt on 2016.5.18.0018.
 */
@Repository("adminDao")
public class AdminDao extends BaseDao {

    /**
     * 根据登录名返回管理员
     *
     * @param loginName
     * @return
     */
    public Admin getAdminByLogin(String loginName) {
        return mongoTemplate.findOne(new Query(Criteria.where("loginName").is(loginName)), Admin.class);
    }

    /**
     * 根据id返回管理员
     *
     * @param adminId
     * @return
     */
    public Admin getAdminById(String adminId) {
        return mongoTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(adminId))), Admin.class);
    }

    /**
     * 分页返回未激活的医生
     *
     * @param quickPager
     */
    public void getApplyDoc(QuickPager<Doctor> quickPager) {
        Query query = new Query(Criteria.where("active").is("0"));
        long count = mongoTemplate.count(query, Doctor.class);
        quickPager.setTotalRows(Integer.parseInt(String.valueOf(count)));
        query.with(new Sort(Sort.Direction.ASC, "regTime")).skip(quickPager.getBeginNum()).limit(quickPager.getPageSize());
        List<Doctor> list = mongoTemplate.find(query, Doctor.class);
        quickPager.setData(list);
    }

    /**
     * 激活医生
     *
     * @param doctorId
     * @return
     */
    public boolean activeDoc(String doctorId) {
        WriteResult wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(doctorId)).and("active").is("0"))
                , new Update().set("active", "1").set("status", "1"), Doctor.class);
        return wr.getN() == 1;
    }

    /**
     * 删除未通过医生
     *
     * @param doctorId
     * @return
     */
    public boolean delDoc(String doctorId) {
        WriteResult wr = mongoTemplate.remove(new Query(Criteria.where("_id").is(new ObjectId(doctorId)).and("active").is("0"))
                , Doctor.class);
        return wr.getN() == 1;
    }
}
