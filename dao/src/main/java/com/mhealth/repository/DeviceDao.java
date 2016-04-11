package com.mhealth.repository;

import com.mhealth.common.base.BaseDao;
import com.mhealth.model.Device;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by pengt on 2016.4.11.0011.
 */
@Repository("deviceDao")
public class DeviceDao extends BaseDao {

    public String insertDevice(Device device) {
        mongoTemplate.insert(device);
        return device.getId();
    }

    public boolean checkDeviceByTag(String id) {
        return mongoTemplate.count(new Query(Criteria.where("_id").is(id)), Device.class) == 0 ? true : false;
    }

    public Device getDevice(String id){
        return mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)),Device.class);
    }
}
