package com.mhealth.controller;

import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.SportRecord;
import com.mhealth.service.SportRecordService;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by pengt on 2016.4.12.0012.
 */
@Controller
@RequestMapping("sportRecord")
public class SportRecordController {

    @Resource(name = "sportRecordService")
    private SportRecordService sportRecordService;

    @RequestMapping(value = "insertRecord", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String insertRecord(String dataList) {
        if (StringUtils.isEmpty(dataList)) return Response.paramsIsEmpty("健康数据");
        JSONArray ja = JSONArray.fromObject(dataList);
        List datas = (List) JSONArray.toList(ja, new SportRecord(), new JsonConfig());
        sportRecordService.insertRecord(datas);
        return new Response().setMessage("上传成功！").toJson();
    }

    @RequestMapping(value = "getAllByTime", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllRecordByTime(String userId, String minTime, String maxTime) {
        if (StringUtils.isEmpty(userId, minTime, maxTime)) return Response.paramsIsEmpty("查询条件：用户id，时间段");
        List<SportRecord> records = sportRecordService.getSportRecords(userId, minTime, maxTime);
        if (records == null) return Response.failuer("记录不存在！");
        return new Response().addList("records", records).toJson();
    }
}
