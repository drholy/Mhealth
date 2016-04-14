package com.mhealth.controller;

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
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        List<AverageHeartRate> avgList = new ArrayList<>();
        List<SumVal> sumList = new ArrayList<>();

        int dTime=0; //时间条件（日周月年）
        int nextTime=0;

        switch(Integer.parseInt(timeUnit)){
            case 0:
                dTime=Calendar.DAY_OF_MONTH;
                nextTime=Calendar.HOUR_OF_DAY;
                cycle = 25;
                break;
            case 1:
                cal.set(Calendar.HOUR_OF_DAY,0);
                dTime=Calendar.WEEK_OF_MONTH;
                nextTime=Calendar.DAY_OF_MONTH;
                cycle = 8;
                break;
            case 2:
                cal.set(Calendar.HOUR_OF_DAY,0);
                dTime=Calendar.MONTH;
                nextTime=Calendar.DAY_OF_MONTH;
                cycle=32;
                break;
            case 3:
                cal.set(Calendar.HOUR_OF_DAY,0);
                dTime=Calendar.YEAR;
                nextTime=Calendar.MONTH;
                cycle=13;
                break;
            default:
                return Response.failuer("时间周期错误！");

        }

        cal.add(dTime, -1);//根据时间周期（日周月年）将日历移动到起点
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

            sumList = sportRecordService.getSum(userId, minTime, maxTime, "stepCount");
            steps[i] = (sumList == null || sumList.size() == 0) ? 0 : sumList.get(0).getSumVal();

            sumList = sportRecordService.getSum(userId, minTime, maxTime, "distance");
            distances[i] = (sumList == null || sumList.size() == 0) ? 0 : sumList.get(0).getSumVal();

            sumList = sportRecordService.getSum(userId, minTime, maxTime, "elevation");
            eles[i] = (sumList == null || sumList.size() == 0) ? 0 : sumList.get(0).getSumVal();

            xTime[i] = cal.get(nextTime) - 1;
            minTime = upTime;
        }
        if(timeUnit.equals("2")){
            for(long x : xTime){
                x+=1;
            }
        }
        return new Response().addObject("avgHeart", avg).addObject("sumStep", steps).addObject("sumDistance", distances).addObject("sumELe", eles)
                .addObject("xTime", xTime).toJson();
    }
}
