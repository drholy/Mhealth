package com.mhealth.repository;

import com.mhealth.common.base.BaseDao;
import com.mhealth.model.SportRecord;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pengt on 2016.4.12.0012.
 */
@Repository("sportRecordDao")
public class SportRecordDao extends BaseDao {

    /**
     * 插入多条记录
     *
     * @param list
     */
    public void insertRecord(List<SportRecord> list) {
        mongoTemplate.insertAll(list);
    }

    /**
     * 根据用户id，时间段查询记录实体
     *
     * @param userId
     * @param minTime
     * @param maxTime
     * @return
     */
    public List<SportRecord> getSportRecords(String userId, String minTime, String maxTime) {
        return mongoTemplate.find(new Query(Criteria.where("userId").is(userId).and("beginTime").gte(minTime).lte(maxTime)), SportRecord.class);
    }
}
