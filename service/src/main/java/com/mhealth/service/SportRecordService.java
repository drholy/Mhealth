package com.mhealth.service;

import com.mhealth.model.SportRecord;
import com.mhealth.repository.SportRecordDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public List<SportRecord> getSportRecords(String userId, String minTime, String maxTime) {
        return sportRecordDao.getSportRecords(userId, minTime, maxTime);
    }
}
