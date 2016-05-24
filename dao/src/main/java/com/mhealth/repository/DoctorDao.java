package com.mhealth.repository;

import com.mhealth.common.base.BaseDao;
import com.mhealth.common.entity.QuickPager;
import com.mhealth.model.CommTrans;
import com.mhealth.model.Comment;
import com.mhealth.model.Doctor;
import com.mhealth.model.User;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.HashMap;
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
        Doctor dbDoctor = mongoTemplate.findOne(new Query(Criteria.where("loginName").is(doctor.getLoginName())), Doctor.class);
        return dbDoctor.getId();
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
        query.with(new Sort(Sort.Direction.DESC, "regTime"))
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
     * 取消医生
     *
     * @param userId
     * @param doctorId
     * @return
     */
    public boolean cancelDoc(String userId, String doctorId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        WriteResult wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(doctorId)))
                , new Update().pull("userList", map), Doctor.class);
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

    /**
     * 修改医生密码
     *
     * @param doctor
     * @return
     */
    public boolean changePasswd(Doctor doctor) {
        WriteResult wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(doctor.getId())))
                , new Update().set("password", doctor.getPassword()), Doctor.class);
        return wr.getN() == 1;
    }

    /**
     * 修改资料
     *
     * @param doctor
     * @return
     */
    public boolean modify(Doctor doctor) {
        WriteResult wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(doctor.getId())))
                , new Update().set("realName", doctor.getRealName()).set("organization", doctor.getOrganization())
                        .set("office", doctor.getOffice()).set("mobilePhone", doctor.getMobilePhone()).set("email", doctor.getEmail())
                        .set("certificate", doctor.getCertificate()).set("headImg", doctor.getHeadImg()), Doctor.class);
        return wr.getN() == 1;
    }

    /**
     * 提交评论
     *
     * @param comment
     * @return
     */
    public boolean comment(Comment comment) {
        CommTrans commTrans = new CommTrans();
        commTrans.setUserId(comment.getUserId());
        commTrans.setDoctorId(comment.getDoctorId());
        commTrans.setComment(comment);
        commTrans.setState("initial");
        commTrans.setLastModified(System.currentTimeMillis());
        try {
            //初始化事务并得到事务id
            mongoTemplate.insert(commTrans);
            commTrans = mongoTemplate.findOne(new Query(Criteria.where("state").is("initial")), CommTrans.class); //得到事务id
            if (commTrans == null) return false;

            //事务状态改为pending
            //In the update statement, the state: "initial" condition ensures that no other process has already updated this record.
            WriteResult wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(commTrans.getId())).and("state").is("initial"))
                    , new Update().set("state", "pending").set("lastModified", System.currentTimeMillis()), CommTrans.class);
            if (wr.getN() != 1) throw new Exception();

            //申请事务到评论表和医生表并更新
        /*
        In the update condition, include the condition pendingTransactions: { $ne: t._id }
        in order to avoid re-applying the transaction if the step is run more than once.
         */
//            wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(comment.getId()))
//                            .and("pendingTransactions").ne(commTrans.getId()))
//                    , new Update().push("comments", comment).push("pendingTransactions", commTrans.getId()), Comment.class);
            List<String> list = comment.getPendingTransactions();
            list.add(commTrans.getId());
            comment.setPendingTransactions(list);
            mongoTemplate.insert(comment);
            comment = mongoTemplate.findOne(new Query(Criteria.where("userId").is(comment.getUserId())
                    .and("doctorId").is(comment.getDoctorId()).and("time").is(comment.getTime())), Comment.class);
            if (comment == null) throw new Exception();
            Map<String, Object> map = new HashMap<>();
            map.put("id", comment.getUserId());
            wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(comment.getDoctorId()))
                            .and("pendingTransactions").ne(commTrans.getId()))
                    , new Update().pull("userList", map)
                            .push("pendingTransactions", commTrans.getId()), Doctor.class);
            if (wr.getN() != 1) throw new Exception();

            //更改事务状态到applied
            wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(commTrans.getId())).and("state").is("pending"))
                    , new Update().set("state", "applied").set("lastModified", System.currentTimeMillis()), CommTrans.class);
            if (wr.getN() != 1) throw new Exception();

            //将评论表和医生表中的响应事务移除
            wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(comment.getId()))
                            .and("pendingTransactions").is(commTrans.getId()))
                    , new Update().pull("pendingTransactions", commTrans.getId()), Comment.class);
            if (wr.getN() != 1) throw new Exception();
            wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(commTrans.getDoctorId()))
                            .and("pendingTransactions").is(commTrans.getId()))
                    , new Update().pull("pendingTransactions", commTrans.getId()), Doctor.class);
            if (wr.getN() != 1) throw new Exception();

            //将事务状态改为done
            wr = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(commTrans.getId())).and("state").is("applied"))
                    , new Update().set("state", "done").set("lastModified", System.currentTimeMillis()), CommTrans.class);
            if (wr.getN() != 1) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            pendingRecovery(commTrans.getId());
            appliedRecovery(commTrans.getId());
            return false;
        }
        return true;
    }

    /**
     * pending状态恢复
     *
     * @param tId
     */
    public void pendingRecovery(String tId) {
        CommTrans commTrans;
        if (tId != null && !tId.equals("")) {
            commTrans = mongoTemplate.findOne(new Query(Criteria.where("state").is("pending")
                    .and("_id").is(new ObjectId(tId))), CommTrans.class);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.add(Calendar.MINUTE, -5);
            long dateThreshold = cal.getTimeInMillis();

            commTrans = mongoTemplate.findOne(new Query(Criteria.where("state").is("pending")
                    .and("lastModifid").lt(dateThreshold)), CommTrans.class);
        }
        if (commTrans == null) return;

        //将事务状态由pending转换到canceling
        mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(commTrans.getId())).and("state").is("pending"))
                , new Update().set("state", "canceling").set("lastModified", System.currentTimeMillis()), CommTrans.class);

        //撤销评论表和医生表的操作
        /*
         In the update condition, include the condition pendingTransactions: t._id in order to update the account
         only if the pending transaction has been applied.
         */
//        mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(commTrans.getComment().getUserId())
//                        .and("doctorId").is(commTrans.getComment().getDoctorId()).and("time").is(commTrans.getComment().getTime())
//                        .and("pendingTransactions").is(commTrans.getId()))
//                , new Update().pull("comments", commTrans.getComment()).pull("pendingTransactions", commTrans.getId()), Comment.class);
        mongoTemplate.remove(new Query(Criteria.where("userId").is(commTrans.getComment().getUserId())
                .and("doctorId").is(commTrans.getComment().getDoctorId()).and("time").is(commTrans.getComment().getTime())
                .and("pendingTransactions").is(commTrans.getId())), Comment.class);

        mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(commTrans.getDoctorId()))
                        .and("pendingTransactions").is(commTrans.getId()))
                , new Update().push("userList", new HashMap<String, Object>().put("id", commTrans.getUserId()))
                        .pull("pendingTransactions", commTrans.getId()), Doctor.class);

        //将事务状态更改为canceled
        mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(commTrans.getId())).and("state").is("canceling"))
                , new Update().set("state", "canceled").set("lastModified", System.currentTimeMillis()), CommTrans.class);
    }

    /**
     * applied状态恢复
     *
     * @param tId
     */
    public void appliedRecovery(String tId) {
        //将当前事务完成
        CommTrans commTrans;
        if (tId != null && !tId.equals("")) {
            commTrans = mongoTemplate.findOne(new Query(Criteria.where("state").is("applied")
                    .and("_id").is(new ObjectId(tId))), CommTrans.class);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.add(Calendar.MINUTE, -5);
            long dateThreshold = cal.getTimeInMillis();

            commTrans = mongoTemplate.findOne(new Query(Criteria.where("state").is("applied")
                    .and("lastModifid").lt(dateThreshold)), CommTrans.class);
        }
        if (commTrans == null) return;

        //将用户表和医生表中的响应事务移除
        mongoTemplate.updateFirst(new Query(Criteria.where("userId").is(commTrans.getComment().getUserId())
                        .and("doctorId").is(commTrans.getComment().getDoctorId()).and("time").is(commTrans.getComment().getTime())
                        .and("pendingTransactions").is(commTrans.getId()))
                , new Update().pull("pendingTransactions", commTrans.getId()), Comment.class);
        mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(commTrans.getDoctorId()))
                        .and("pendingTransactions").is(commTrans.getId()))
                , new Update().pull("pendingTransactions", commTrans.getId()), Doctor.class);

        //将事务状态改为done
        mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(commTrans.getId())).and("state").is("applied"))
                , new Update().set("state", "done").set("lastModified", System.currentTimeMillis()), CommTrans.class);
    }
}
