package com.mhealth.controller;

import com.mhealth.common.entity.QuickPager;
import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.SportRecord;
import com.mhealth.model.Token;
import com.mhealth.model.User;
import com.mhealth.service.SportRecordService;
import com.mhealth.service.TokenService;
import com.mhealth.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pengt on 2016.5.31.0031.
 */
@Controller
@RequestMapping("sync")
public class SyncController {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "sportRecordService")
    private SportRecordService sportRecordService;

    @Resource(name = "tokenService")
    private TokenService tokenService;

    /**
     * 将数据同步到本系统
     *
     * @param dataJson
     * @return
     */
    @RequestMapping(value = "putData", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String putData(String dataJson) {
        if (StringUtils.isEmpty(dataJson)) return Response.paramsIsEmpty("dataJson");
        Map<String, String> dataMap;
        try {
            dataMap = (Map<String, String>) JSONObject.toBean(JSONObject.fromObject(dataJson), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failuer("dataJson格式不正确！");
        }
        if (StringUtils.isEmpty(dataMap.get("userId"), dataMap.get("username")
                , dataMap.get("heartRate"), dataMap.get("time"), dataMap.get("access_token")))
            return Response.paramsIsEmpty("体征数据");
        Token token = tokenService.getTokenByAcc(dataMap.get("access_token"));
        if (token == null) return Response.failuer("token错误！");
        if (userService.getUserById(dataMap.get("userId")) == null) {
            User user = new User();
            user.setId(dataMap.get("userId"));
            user.setLoginName(dataMap.get("username"));
            user.setRegTime(System.currentTimeMillis());
            user.setActive("0");
            user.setStatus("0");
            userService.insertUser(user);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = simpleDateFormat.parse(dataMap.get("time"));
        } catch (ParseException e) {
            e.printStackTrace();
            return Response.failuer("时间格式错误！");
        }
        long time = date.getTime();
        SportRecord sportRecord = new SportRecord();
        sportRecord.setUserId(dataMap.get("userId"));
        sportRecord.setSport_heartRate(Long.parseLong(dataMap.get("heartRate")));
        sportRecord.setBeginTime(time - 12000); //测量周期为12秒
        sportRecord.setEndTime(time);
        sportRecord.setUploadTime(System.currentTimeMillis());
        List<SportRecord> list = new ArrayList<>();
        list.add(sportRecord);
        sportRecordService.insertRecord(list);
        return Response.success("上传成功！");
    }

    /**
     * 分页返回用户数据
     *
     * @param currPage
     * @param pageSize
     * @param userId
     * @param access_token
     * @return
     */
    @RequestMapping(value = "getData", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getData(String currPage, String pageSize, String userId, String access_token) {
        if (StringUtils.isEmpty(userId, access_token)) return Response.paramsIsEmpty("userId,access_token");
        Token token = tokenService.getTokenByAcc(access_token);
        if (token == null) return Response.failuer("token错误！");
        QuickPager<SportRecord> quickPager = new QuickPager<>(currPage, pageSize);
        sportRecordService.getAllRecords(quickPager, userId);
        return new Response().toPageJson(quickPager);
    }
}
