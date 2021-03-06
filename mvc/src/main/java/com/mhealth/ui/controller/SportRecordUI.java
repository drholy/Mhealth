package com.mhealth.ui.controller;

import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pengt on 2016.4.13.0013.
 */
@Controller
@RequestMapping("record")
public class SportRecordUI {

    /**
     * 概览页面
     *
     * @return
     */
    @RequestMapping("overview.ui")
    public String overview(String id, ModelMap modelMap) {
        modelMap.put("id", id);
        return "sportRecord/recordOverview";
    }

    @RequestMapping("recordByDay.ui")
    public String recordByDay(String id, String key, String time, ModelMap modelMap) {
        if (StringUtils.isEmpty(key)) return null;
        if (StringUtils.isEmpty(time)) time = String.valueOf(System.currentTimeMillis());
        modelMap.put("id", id);
        modelMap.put("key", key);
        modelMap.put("time", time);
        return "sportRecord/recordByDay";
    }

    @RequestMapping("recordByWeek.ui")
    public String recordByWeek(String id, String key, String time, ModelMap modelMap) {
        if (StringUtils.isEmpty(key)) return null;
        if (StringUtils.isEmpty(time)) time = String.valueOf(System.currentTimeMillis());
        modelMap.put("id", id);
        modelMap.put("key", key);
        modelMap.put("time", time);
        return "sportRecord/recordByWeek";
    }

    @RequestMapping("recordByMonth.ui")
    public String recordByMonth(String id, String key, String time, ModelMap modelMap) {
        if (StringUtils.isEmpty(key)) return null;
        if (StringUtils.isEmpty(time)) time = String.valueOf(System.currentTimeMillis());
        modelMap.put("id", id);
        modelMap.put("key", key);
        modelMap.put("time", time);
        return "sportRecord/recordByMonth";
    }

    /**
     * 按年显示记录
     *
     * @return
     */
    @RequestMapping("recordByYear.ui")
    public String recordByYear(String id, String key, String time, ModelMap modelMap) {
        if (StringUtils.isEmpty(key)) return null;
        if (StringUtils.isEmpty(time)) time = String.valueOf(System.currentTimeMillis());
        modelMap.put("id", id);
        modelMap.put("key", key);
        modelMap.put("time", time);
        return "sportRecord/recordByYear";
    }

    @RequestMapping("recordByBTime.ui")
    public String recordByBTime(String id, String key, String time, ModelMap modelMap) {
        if (StringUtils.isEmpty(key)) return null;
        if (StringUtils.isEmpty(time)) time = String.valueOf(System.currentTimeMillis());
        modelMap.put("id", id);
        modelMap.put("key", key);
        modelMap.put("time", time);
        return "sportRecord/recordDetail";
    }

    @RequestMapping("allRecords.ui")
    public String allRecords(String id, ModelMap modelMap) {
        modelMap.put("id", id);
        return "sportRecord/allRecord";
    }
}
