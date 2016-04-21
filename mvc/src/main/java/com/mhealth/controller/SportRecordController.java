package com.mhealth.controller;

import com.mhealth.common.entity.QuickPager;
import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.AverageHeartRate;
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
    public String getAllRecordByTime(String userId, String timeUnit) {
        if (StringUtils.isEmpty(userId, timeUnit)) return Response.paramsIsEmpty("查询条件：用户id，时间段");
        long minTime = 0;
        long maxTime = System.currentTimeMillis();
        int cycle = 0;//需要查询的次数

        //日历设为当天0分0秒0毫秒
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(maxTime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        List<AverageHeartRate> avgList;
        List<SumVal> sumList;

        //int dTime = 0; //时间条件（日周月年）
        int nextTime = 0;

        switch (Integer.parseInt(timeUnit)) {
            case 0:
                //dTime = Calendar.DAY_OF_MONTH;
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
                return Response.failuer("时间周期错误！");

        }

        //cal.add(dTime, -1);//根据时间周期（日周月年）将日历移动到起点
        minTime = cal.getTimeInMillis();
        long avg[] = new long[cycle];
        long steps[] = new long[cycle];
        long distances[] = new long[cycle];
        long eles[] = new long[cycle];
        long xTime[] = new long[cycle];
        for (int i = 0; i < cycle; i++) {
            cal.setTimeInMillis(minTime);
            cal.add(nextTime, +1);//日历向后移动一个时间单位（日周月年）
            long upTime = cal.getTimeInMillis();

            avgList = sportRecordService.getAverHr(userId, minTime, upTime);
            avg[i] = (avgList == null || avgList.size() == 0) ? 0 : avgList.get(0).getAverage();

            sumList = sportRecordService.getSum(userId, minTime, upTime, "stepCount");
            steps[i] = (sumList == null || sumList.size() == 0) ? 0 : sumList.get(0).getSumVal();

            sumList = sportRecordService.getSum(userId, minTime, upTime, "distance");
            distances[i] = (sumList == null || sumList.size() == 0) ? 0 : sumList.get(0).getSumVal();

            sumList = sportRecordService.getSum(userId, minTime, upTime, "elevation");
            eles[i] = (sumList == null || sumList.size() == 0) ? 0 : sumList.get(0).getSumVal();

            xTime[i] = cal.get(nextTime) - 1;
            minTime = upTime;
        }
        if (timeUnit.equals("3")) { //calendar返回的月份数会比实际的小1
            for (int i = 0; i < xTime.length; i++) {
                xTime[i] += 1;
            }
        }
        return new Response().addObject("avgHeart", avg).addObject("sumStep", steps).addObject("sumDistance", distances).addObject("sumELe", eles)
                .addObject("xTime", xTime).toJson();
    }

    @RequestMapping(value = "getRecordByTime", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getRecordByTime(String userId, String key, String beginTime, String timeUnit) {
        if (StringUtils.isEmpty(userId, key, beginTime, timeUnit)) return Response.paramsIsEmpty("查询条件");
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
        long xTime[] = new long[cycle];
        long minTime = cal.getTimeInMillis();
        if (key.equals("sport_heartRate")) {
            for (int i = 0; i < cycle; i++) {
                cal.setTimeInMillis(minTime);
                cal.add(nextTime, +1);
                long upTime = cal.getTimeInMillis();

                List<AverageHeartRate> avgList = sportRecordService.getAverHr(userId, minTime, upTime);
                result[i] = (avgList == null || avgList.size() == 0) ? 0 : avgList.get(0).getAverage();
                xTime[i] = minTime;
                minTime = upTime;
            }
        } else {
            for (int i = 0; i < cycle; i++) {
                cal.setTimeInMillis(minTime);
                cal.add(nextTime, +1);
                long upTime = cal.getTimeInMillis();

                List<SumVal> sumList = sportRecordService.getSum(userId, minTime, upTime, key);
                result[i] = (sumList == null || sumList.size() == 0) ? 0 : sumList.get(0).getSumVal();
                xTime[i] = minTime;
                minTime = upTime;
            }
        }
        return new Response().addObject("result", result).addObject("xTime", xTime).toJson();
    }

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

    @RequestMapping(value = "allRecords", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllRecords(String userId, String currPage, String pageSize) {
        if (StringUtils.isEmpty(userId)) return Response.paramsIsEmpty("用户id");
        QuickPager<SportRecord> quickPager = new QuickPager<SportRecord>(currPage, pageSize);
        sportRecordService.getAllRecords(quickPager, userId);
        return new Response().toPageJson(quickPager);
    }
}
