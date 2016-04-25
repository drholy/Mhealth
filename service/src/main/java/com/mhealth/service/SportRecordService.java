package com.mhealth.service;

import com.mhealth.common.entity.QuickPager;
import com.mhealth.model.AverageHeartRate;
import com.mhealth.model.AvgVal;
import com.mhealth.model.SportRecord;
import com.mhealth.model.SumVal;
import com.mhealth.repository.SportRecordDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * Created by pengt on 2016.4.12.0012.
 */
@Service("sportRecordService")
public class SportRecordService {

    @Resource(name = "sportRecordDao")
    private SportRecordDao sportRecordDao;

    public void insertRecord(List<SportRecord> list) {
        sportRecordDao.insertRecord(list);
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
        return sportRecordDao.getSportRecords(userId, minTime, maxTime);
    }

    public List<AverageHeartRate> getAverHr(String userId, long minTime, long maxTime) {
        return sportRecordDao.getAverHr(userId, minTime, maxTime);
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
        return sportRecordDao.getAvgVal(userId, key, minTime, maxTime);
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
        return sportRecordDao.getSumVal(userId, key, minTime, maxTime);
    }

    /**
     * 根据userId分页查询用户所有体征值
     *
     * @param quickPager
     * @param userId
     */
    public void getAllRecords(QuickPager<SportRecord> quickPager, String userId) {
        sportRecordDao.getAllRecords(quickPager, userId);
    }
}
