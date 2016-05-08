package com.mhealth.controller;

import com.mhealth.common.entity.QuickPager;
import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.AverageHeartRate;
import com.mhealth.model.AvgVal;
import com.mhealth.model.SportRecord;
import com.mhealth.model.SumVal;
import com.mhealth.service.SportRecordService;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by pengt on 2016.4.12.0012.
 */
@Controller
@RequestMapping("sportRecord")
public class SportRecordController {

    @Resource(name = "sportRecordService")
    private SportRecordService sportRecordService;

    /**
     * 插入多条运动记录
     *
     * @param dataList
     * @return
     */
    @RequestMapping(value = "insertRecord", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String insertRecord(String dataList) {
        if (StringUtils.isEmpty(dataList)) return Response.paramsIsEmpty("健康数据");
        JSONArray ja = JSONArray.fromObject(dataList);
        List datas = (List) JSONArray.toList(ja, new SportRecord(), new JsonConfig());
        sportRecordService.insertRecord(datas);
        return Response.success("上传成功！");
    }

    /**
     * 根据时间列出详细记录
     *
     * @param userId
     * @param key
     * @param beginTime
     * @return
     */
    @RequestMapping(value = "getRecordByBTime", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getRecordByBt(String userId, String key, String beginTime) {
        if (StringUtils.isEmpty(userId, key, beginTime)) Response.paramsIsEmpty("查询条件！");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(beginTime));
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long minTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, +1);
        long maxTime = cal.getTimeInMillis();
        List<SportRecord> records = sportRecordService.getSportRecords(userId, minTime, maxTime);
        return new Response().addList("result", records).toJson();
    }

    /**
     * 分页列出指定用户所有记录
     *
     * @param userId
     * @param currPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "allRecords", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllRecords(String userId, String currPage, String pageSize) {
        if (StringUtils.isEmpty(userId)) return Response.paramsIsEmpty("用户id");
        QuickPager<SportRecord> quickPager = new QuickPager<SportRecord>(currPage, pageSize);
        sportRecordService.getAllRecords(quickPager, userId);
        return new Response().toPageJson(quickPager);
    }

    /**
     * 根据指定用户、数据键、开始时间、时间周期查询平均值
     *
     * @param userId
     * @param key
     * @param beginTime 为空时默认为当前时间的时间戳
     * @param timeUnit
     * @return
     */
    @RequestMapping(value = "getAvgVal", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAvgVal(String userId, String key, String beginTime, String timeUnit) {
        if (StringUtils.isEmpty(userId, key, timeUnit)) Response.paramsIsEmpty("userId,key,timeUnit");
        if (StringUtils.isEmpty(beginTime)) beginTime = String.valueOf(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(beginTime));
        //将时分秒毫秒归零
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int nextTime = 0;
        int cycle = 0;

        switch (Integer.valueOf(timeUnit)) {
            case 0:
                nextTime = Calendar.HOUR_OF_DAY;
                cycle = 24;
                break;
            case 1:
                cal.set(Calendar.DAY_OF_WEEK, 1);
                nextTime = Calendar.DAY_OF_WEEK;
                cycle = 7;
                break;
            case 2:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                nextTime = Calendar.DAY_OF_MONTH;
                cycle = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;
            case 3:
                cal.set(Calendar.MONTH, 0);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                nextTime = Calendar.MONTH;
                cycle = 12;
                break;
            default:
                return Response.failuer("时间单位错误！");
        }

        long result[] = new long[cycle];
        String xTime[] = new String[cycle];
        long minTime = cal.getTimeInMillis();
        for (int i = 0; i < cycle; i++) {
            cal.add(nextTime, +1);
            long upTime = cal.getTimeInMillis();

            List<AvgVal> avgList = sportRecordService.getAvgVal(userId, key, minTime, upTime);
            result[i] = (avgList == null || avgList.size() == 0) ? 0 : avgList.get(0).getAvgVal();
            xTime[i] = (avgList == null || avgList.size() == 0) ? "" : String.valueOf(minTime);
            minTime = upTime;
        }
        return new Response().addObject("result", result).addObject("xTime", xTime).addString("timeUnit", timeUnit).toJson();
    }

    /**
     * 根据指定用户、数据键、时间段查询和
     *
     * @param userId
     * @param key
     * @param beginTime 为空时默认为当前时间的时间戳
     * @param timeUnit
     * @return
     */
    @RequestMapping(value = "getSumVal", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getSumVal(String userId, String key, String beginTime, String timeUnit) {
        if (StringUtils.isEmpty(userId, key, timeUnit)) Response.paramsIsEmpty("userId,key,timeUnit");
        if (StringUtils.isEmpty(beginTime)) beginTime = String.valueOf(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(beginTime));
        //将时分秒毫秒归零
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int nextTime = 0;
        int cycle = 0;

        switch (Integer.valueOf(timeUnit)) {
            case 0:
                nextTime = Calendar.HOUR_OF_DAY;
                cycle = 24;
                break;
            case 1:
                cal.set(Calendar.DAY_OF_WEEK, 1);
                nextTime = Calendar.DAY_OF_WEEK;
                cycle = 7;
                break;
            case 2:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                nextTime = Calendar.DAY_OF_MONTH;
                cycle = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;
            case 3:
                cal.set(Calendar.MONTH, 0);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                nextTime = Calendar.MONTH;
                cycle = 12;
                break;
            default:
                return Response.failuer("时间单位错误！");
        }

        long result[] = new long[cycle];
        String xTime[] = new String[cycle];
        long minTime = cal.getTimeInMillis();
        for (int i = 0; i < cycle; i++) {
            cal.add(nextTime, +1);
            long upTime = cal.getTimeInMillis();

            List<SumVal> sumList = sportRecordService.getSumVal(userId, key, minTime, upTime);
            result[i] = (sumList == null || sumList.size() == 0) ? 0 : sumList.get(0).getSumVal();
            xTime[i] = (sumList == null || sumList.size() == 0) ? "" : String.valueOf(minTime);
            minTime = upTime;
        }
        return new Response().addObject("result", result).addObject("xTime", xTime).addString("timeUnit", timeUnit).toJson();
    }
}
