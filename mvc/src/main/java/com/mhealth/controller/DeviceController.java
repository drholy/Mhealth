package com.mhealth.controller;

import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.Device;
import com.mhealth.service.DeviceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by pengt on 2016.4.29.0029.
 */
@Controller
@RequestMapping("deviceData")
public class DeviceController {

    @Resource(name = "deviceService")
    private DeviceService deviceService;

    @RequestMapping(value = "getDevice", produces = {"application/json;charset=UTF-8"})
    public String getDevice(String id) {
        if (StringUtils.isEmpty(id)) return Response.paramsIsEmpty("设备id");
        Device device = deviceService.getDevice(id);
        return new Response().addObject("device", device).toJson();
    }
}
