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
    public String overview() {
        return "sportRecord/recordOverview";
    }

    /**
     * 按年显示记录
     *
     * @return
     */
    @RequestMapping("recordByYear.ui")
    public String recordByYear() {
        return "sportRecord/recordByYear";
    }

    @RequestMapping("recordByDay.ui")
    public String recordByDay(String key,String day,ModelMap modelMap) {
        if (StringUtils.isEmpty(key)) return null;
        if(StringUtils.isEmpty(day)) day=String.valueOf(System.currentTimeMillis());
        modelMap.put("key", key);
        modelMap.put("day",day);
        return "sportRecord/recordByDay";
    }
}
