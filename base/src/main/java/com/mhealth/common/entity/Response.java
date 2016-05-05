package com.mhealth.common.entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import net.sf.json.processors.JsonValueProcessor;
import org.apache.log4j.Logger;

public class Response {

    private static Logger log = Logger.getLogger(Response.class);

    private Map<String, Object> data = null;
    public String resCodeKey = "resCode";
    public String resMsgKey = "resMsg";
    private Map<String, Object> body = null;

    public Response() {
        data = new HashMap<String, Object>(3);
    }

    public final static String SUCCESS = "000000";//成功
    public final static String IS_EXIST = "100101";// 已经存在
    public final static String NOT_EXIST = "100102";//不存在
    public final static String FAILURE = "100001";// 失败提示

    public final static String USERTYPE_ERROR = "200100";//用户类型错误 例如:绑定亲属对方是医生 绑定医生对方是普通用户
    public final static String USERNOTEXIST_ERROR = "200101";//用户不存在错误

    public final static String PARAMS_IS_EMPTY = "100101";// 参数不能为空
    public final static String PARAMS_CHECK_ERROR = "100102";// 参数校验错误
    public final static String ACTIVITE_FAILURE = "100103";// 激活失败
    public final static String DONOT_ACTIVITE = "100104";// 未激活
    public final static String NOT_LOGIN = "100105";      //未登陆
    public final static String TOKEN_EXPIRED = "100106"; //token过期
    public final static String SERVICE_ERROR = "500100";// 服务端系统发生错误

    public final static String ADD_FAILURE = "500101";// 添加数据失败
    public final static String UPDATE_FAILURE = "500102";// 修改数据失败
    public final static String DELETE_FAILURE = "500103";// 删除数据失败

    public final static String NO_PRO = "40001";//没有权限

    /*
     *
     *参悟参数列表
     *    用户登录
     *    100200//用户名或者密码错误
     * 	  100201//用户已经禁用,不可以登陆
     * 	  100400//添加自己的邮箱为亲情用户的邮箱
     * 	  100401//添加其他用户邮箱为亲情用户邮箱
     */
    public static String failuer(String resMsg) {
        return "{\"resCode\":\"" + FAILURE + "\",\"resMsg\":\"" + resMsg + "\",\"data\":{}}";
    }

    public static String isExist(String resMsg) {
        return "{\"resCode\":\"" + IS_EXIST + "\",\"resMsg\":\"" + resMsg + "\",\"data\":{}}";
    }

    public static String notExist(String resMsg) {
        return "{\"resCode\":\"" + NOT_EXIST + "\",\"resMsg\":\"" + resMsg + "\",\"data\":{}}";
    }

    public static String addFailuer(String resMsg) {
        return "{\"resCode\":\"" + ADD_FAILURE + "\",\"resMsg\":\"" + resMsg + "\",\"data\":{}}";
    }

    public static String deleteFailuer(String resMsg) {
        return "{\"resCode\":\"" + DELETE_FAILURE + "\",\"resMsg\":\"" + resMsg + "\",\"data\":{}}";
    }

    public static String updateFailuer(String resMsg) {
        return "{\"resCode\":\"" + UPDATE_FAILURE + "\",\"resMsg\":\"" + resMsg + "\",\"data\":{}}";
    }

    public static String reLogin() {
        return "{\"resCode\":\"020001\",\"resMsg\":\"需要重新登录\",\"data\":{},\"rows\":[],\"total\":\"0\"}";
    }

    public static String getResponseStr(String code, String msg) {
        return "{\"resCode\":\"" + code + "\",\"resMsg\":\"" + msg + "\",\"data\":{}}";
    }

    public static String paramsIsEmptyError(String msg) {
        return getResponseStr(PARAMS_IS_EMPTY, msg);
    }

    public static String paramsIsEmpty(String params) {
        return paramsIsEmptyError("[" + params + "]不能为空!");
    }

    public static String success(String msg) {
        return getResponseStr(Response.SUCCESS, msg);
    }

    public static String paramsCheckError(String msg) {
        return getResponseStr(PARAMS_CHECK_ERROR, msg);
    }

    public Response setError(String resCode, String resMsg) {
        data.put(resCodeKey, resCode);
        data.put(resMsgKey, resMsg);
        return this;
    }

    public Response setMessage(String resMsg) {
        data.put(resMsgKey, resMsg);
        return this;
    }

    public static String error(String code, String msg) {
        return getResponseStr(code, msg);
    }


    public Response addString(String key, String value) {
        if (this.body == null) {
            this.body = new HashMap<String, Object>(1);
        }
        this.body.put(key, value);
        return this;
    }

    public Response addList(String key, List<?> list) {
        if (this.body == null) {
            this.body = new HashMap<String, Object>();
        }
        this.body.put(key, list);
        this.body.put(key + "Size", (list == null) ? "0" : list.size());
        return this;
    }

    public Response addMap(Map<String, ?> map) {
        if (this.body == null) {
            this.body = new HashMap<String, Object>(1);
        }
        if (map != null) {
            this.body.putAll(map);
        }
        return this;
    }

    public Response addObject(String name, Object object) {
        if (this.body == null) {
            this.body = new HashMap<String, Object>(1);
        }
        this.body.put(name, object);
        return this;
    }

    /**
     * 返回系统处理出错的异常
     *
     * @param e
     * @return
     */
    public static String systemError(Exception e) {
        return "{\"resCode\":\"" + Response.SERVICE_ERROR + "\",\"resMsg\":\"系统处理出错!\",\"data\":{},\"rows\":[],\"total\":\"0\"}";
    }

    public static String systemErrorHandler(Exception e) {
        log.debug("-------------------------------------->发生了异常<--------------------------------");
        log.error(e, e);

        return "{\"resCode\":\"" + Response.SERVICE_ERROR + "\",\"resMsg\":\"系统处理出错!\",\"data\":{ \"error\":\"" + e.getMessage() + "\"} }";
    }


    public String toJson() {
        if (!this.data.containsKey(resCodeKey)) {
            this.data.put(resCodeKey, Response.SUCCESS);
        }
        if (!this.data.containsKey(resMsgKey)) {
            this.data.put(resMsgKey, "成功");
        }
        if (this.body == null || this.body.isEmpty()) {
            this.data.put("data", new Object());
        } else {
            this.data.put("data", this.body);
        }
        try {

            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(Integer.class, new DataHandler());
            jsonConfig.registerJsonValueProcessor(Long.class, new DataHandler());
            jsonConfig.registerJsonValueProcessor(String.class, new DataHandler());
            return JSONObject.fromObject(this.data, jsonConfig).toString();
        } catch (Exception e) {
            log.error(e, e);
            return getResponseStr(SERVICE_ERROR, "webservice服务端JSON转换出错");
        }
    }

    public String toJson(JsonConfig jsonConfig) {

        if (!this.data.containsKey(resCodeKey)) {
            this.data.put(resCodeKey, Response.SUCCESS);
        }
        if (!this.data.containsKey(resMsgKey)) {
            this.data.put(resMsgKey, "成功");
        }
        if (this.body == null || this.body.isEmpty()) {
            this.data.put("data", new Object());
        } else {
            this.data.put("data", this.body);
        }

        if (jsonConfig == null) jsonConfig = new JsonConfig();

        try {
            jsonConfig.registerJsonValueProcessor(Integer.class, new DataHandler());
            jsonConfig.registerJsonValueProcessor(Long.class, new DataHandler());
            jsonConfig.registerJsonValueProcessor(String.class, new DataHandler());
            return JSONObject.fromObject(this.data, jsonConfig).toString();
        } catch (Exception e) {
            log.error(e, e);
            return getResponseStr(SERVICE_ERROR, "webservice服务端JSON转换出错");
        }
    }

    @Override
    public String toString() {
        return this.toJson();
    }

    public String toPageJson(final QuickPager<?> quickPager) {

        if (!this.data.containsKey(resCodeKey)) {
            this.data.put(resCodeKey, Response.SUCCESS);
        }

        if (!this.data.containsKey(resMsgKey)) {
            this.data.put(resMsgKey, "成功");
        }

        if (this.body == null) {
            this.body = new HashMap<String, Object>(2);
        }
        try {
            this.body.put("currPage", String.valueOf(quickPager.getCurrPage()));// 当前页码
            this.body.put("totalPages", String.valueOf(quickPager.getTotalPages()));// 总页数
            this.body.put("countSize", String.valueOf(quickPager.getTotalRows()));// 总记录数
            this.body.put("pageSize", String.valueOf(quickPager.getMaxLine()));// 每页的条数
            this.addObject("rows", (quickPager.getData() == null ? new ArrayList<Object>(0) : quickPager.getData()));
            this.data.put("data", this.body);
            return this.toJson();

        } catch (Exception e) {
            log.error(e, e);
            return new Response().addObject("total", new Integer(0)).addObject("rows", new ArrayList<Object>(0)).setMessage("成功").toJson();
        }
    }

    public String toPageJson(final QuickPager<?> quickPager, JsonConfig jsonConfig) {
        if (!this.data.containsKey(resCodeKey)) {
            this.data.put(resCodeKey, Response.SUCCESS);
        }
        if (!this.data.containsKey(resMsgKey)) {
            this.data.put(resMsgKey, "成功");
        }
        if (this.body == null) {
            this.body = new HashMap<String, Object>(2);
        }
        try {
            this.body.put("currPage", String.valueOf(quickPager.getCurrPage()));// 当前页码
            this.body.put("totalPages", String.valueOf(quickPager.getTotalPages()));// 总页数
            this.body.put("countSize", String.valueOf(quickPager.getTotalRows()));// 总记录数
            this.body.put("pageSize", String.valueOf(quickPager.getMaxLine()));// 每页的条数
            this.addObject("rows", (quickPager.getData() == null ? new ArrayList<Object>(0) : quickPager.getData()));
            this.data.put("data", this.body);
            return this.toJson(jsonConfig);
        } catch (Exception e) {
            log.error(e, e);
            return new Response().addObject("total", new Integer(0)).addObject("rows", new ArrayList<Object>(0)).setMessage("服务器转换json失败").toJson();
        }
    }

    private class DataHandler implements JsonValueProcessor {
        @Override
        public Object processArrayValue(Object o, JsonConfig jsonConfig) {
            return o;
        }

        @Override
        public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
            if (o == null) return "";
            if (o instanceof Integer || o instanceof Long || o instanceof String) {
                return String.valueOf(o);
            } else {
                return o;
            }
        }
    }


}