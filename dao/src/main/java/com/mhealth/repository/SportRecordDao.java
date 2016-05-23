package com.mhealth.repository;

import com.mhealth.common.base.BaseDao;
import com.mhealth.common.entity.QuickPager;
import com.mhealth.model.AverageHeartRate;
import com.mhealth.model.AvgVal;
import com.mhealth.model.SportRecord;
import com.mhealth.model.SumVal;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
    public List<SportRecord> getSportRecords(String userId, long minTime, long maxTime) {
        return mongoTemplate.find(new Query(Criteria.where("userId").is(userId).and("beginTime").gte(minTime).lte(maxTime))
                .with(new Sort(Sort.Direction.ASC, "beginTime")), SportRecord.class);
    }

    /**
     * 根据条件得到心率平均值
     *
     * @param userId
     * @param minTime
     * @param maxTime
     * @return
     */
    public List<AverageHeartRate> getAverHr(String userId, long minTime, long maxTime) {
        Criteria c = Criteria.where("userId").is(userId).and("beginTime").gte(minTime).lt(maxTime);
        Aggregation agg = Aggregation.newAggregation(Aggregation.match(c)
                , Aggregation.project("sport_heartRate", "userId")
                , Aggregation.group("userId").avg("sport_heartRate").as("average")
                , Aggregation.project("average").and("userId").previousOperation());
        AggregationResults<AverageHeartRate> results = mongoTemplate.aggregate(agg, "sportRecord", AverageHeartRate.class);
        return results.getMappedResults();
    }

    /**
     * 根据指定用户、数据键、时间段查询平均值
     *
     * @param userId
     * @param key
     * @param minTime
     * @param maxTime
     * @return
     */
    public List<AvgVal> getAvgVal(String userId, String key, long minTime, long maxTime) {
        Criteria c = Criteria.where("userId").is(userId).and("beginTime").gte(minTime).lt(maxTime);
        Aggregation agg = Aggregation.newAggregation(Aggregation.match(c)
                , Aggregation.project("userId", key)
                , Aggregation.group("userId").avg(key).as("avgVal"));
        AggregationResults<AvgVal> results = mongoTemplate.aggregate(agg, "sportRecord", AvgVal.class);
        return results.getMappedResults();
    }


    /**
     * 根据指定用户、数据键、时间段查询和
     *
     * @param userId
     * @param key
     * @param minTime
     * @param maxTime
     * @return
     */
    public List<SumVal> getSumVal(String userId, String key, long minTime, long maxTime) {
        Criteria c = Criteria.where("userId").is(userId).and("beginTime").gte(minTime).lt(maxTime);
        Aggregation agg = Aggregation.newAggregation(Aggregation.match(c)
                , Aggregation.project(key, "userId")
                , Aggregation.group("userId").sum(key).as("sumVal"));
        AggregationResults<SumVal> results = mongoTemplate.aggregate(agg, "sportRecord", SumVal.class);
        return results.getMappedResults();
    }

    /**
     * 根据userId分页查询用户所有体征值
     *
     * @param quickPager
     * @param userId
     */
    public void getAllRecords(QuickPager<SportRecord> quickPager, String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        long count = mongoTemplate.count(query, SportRecord.class);
        quickPager.setTotalRows(Integer.parseInt(String.valueOf(count)));
        query.with(new Sort(Sort.Direction.DESC, "beginTime")).skip(quickPager.getBeginNum()).limit(quickPager.getPageSize());
        List<SportRecord> records = mongoTemplate.find(query, SportRecord.class);
        quickPager.setData(records);
    }
}
