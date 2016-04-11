package com.mhealth.service;

import com.mhealth.model.Device;
import com.mhealth.repository.DeviceDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by pengt on 2016.4.11.0011.
 */
@Service("deviceService")
public class DeviceService {

    @Resource(name="deviceDao")
    private DeviceDao deviceDao;

    public String insertDevice(Device device) {
        return deviceDao.insertDevice(device);
    }

    public boolean checkDeviceByTag(String id) {
        return deviceDao.checkDeviceByTag(id);
    }

    public Device getDevice(String id){
        return deviceDao.getDevice(id);
    }
}
