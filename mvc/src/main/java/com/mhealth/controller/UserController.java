package com.mhealth.controller;

import com.mhealth.common.entity.Response;
import com.mhealth.common.util.PasswordUtils;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.Device;
import com.mhealth.model.User;
import com.mhealth.service.DeviceService;
import com.mhealth.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;
import java.util.UUID;

/**
 * Created by pengt on 2016.4.6.0006.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "deviceService")
    private DeviceService deviceService;

    /**
     * 用户注册
     *
     * @param userJson
     * @return
     */
    @RequestMapping(value = "insertUser", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String insertUser(String userJson) {
        if (userJson == null || userJson.equals("")) return Response.paramsIsEmpty("User data");
        JSONObject jsonObject = JSONObject.fromObject(userJson);
        String loginName = jsonObject.get("loginName").toString();
        if (!userService.checkUserByLn(loginName)) return Response.isExist("用户已存在！");
        if (jsonObject.get("password").toString().length() < 6) return Response.failuer("密码少于6位！");
        String id = UUID.randomUUID().toString();

        User user = new User();
        user.setId(id);
        user.setLoginName(jsonObject.get("loginName").toString());
        user.setUsername(jsonObject.get("username").toString());
        user.setUserType("0");
        user.setActive("0");
        user.setRegTime(String.valueOf(System.currentTimeMillis()));
        user.setStatus("0");
        try {
            user.setPassword(PasswordUtils.getEncryptedPwd((String) jsonObject.get("password")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (userService.insertUser(user).equals(id))
            return new Response().addString("loginName", user.getLoginName()).setMessage("注册成功").toJson();
        else return Response.addFailuer("注册失败！");
    }

    /**
     * 根据用户名返回用户
     *
     * @param loginName
     * @return
     */
    @RequestMapping(value = "getUser", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getUser(String loginName) {
        if (StringUtils.isEmpty(loginName)) return Response.paramsIsEmpty("登录名为空!");
        return new Response().addObject("user", userService.getUser(loginName)).toJson();
    }

    /**
     * 登录
     *
     * @param loginName
     * @param password
     * @param deviceJson
     * @param request
     * @return
     */
    @RequestMapping(value = "login", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String login(String loginName, String password, String deviceJson, HttpServletRequest request) {
        if (StringUtils.isEmpty(loginName, password, deviceJson)) return Response.paramsIsEmpty("用户名；密码；设备信息！");
        User dbUser = userService.getUser(loginName);
        if (dbUser == null) return Response.failuer("用户不存在！");
        try {
            if (!PasswordUtils.validPasswd(password, dbUser.getPassword())) return Response.failuer("用户名或密码错误！");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (dbUser.getActive().equals("0"))
            return new Response().setError(Response.FAILURE, "账户未激活！").addString("url", "#").toJson();
        if (dbUser.getStatus().equals("0")) return new Response().setError(Response.FAILURE, "账户异常，请与管理员联系！").toJson();
        String newUrl = "";
        if (dbUser.getUserType().equals("0")) newUrl = "#";
        request.getSession().setAttribute("user", dbUser);

        JSONObject device = JSONObject.fromObject(deviceJson);
        Device dbDevice = deviceService.getDevice(device.get("id").toString());
        if (dbDevice == null) {
            dbDevice.setId(device.get("id").toString());
            dbDevice.setUserId(device.get("userId").toString());
            dbDevice.setName(device.get("name").toString());
            dbDevice.setBrand(device.get("brand").toString());
            dbDevice.setModel(device.get("model").toString());
            dbDevice.setOs(device.get("os").toString());
            dbDevice.setType(device.get("type").toString());
            dbDevice.setStatus(device.get("status").toString());
            deviceService.insertDevice(dbDevice);
        }
        request.getSession().setAttribute("device", dbDevice);
        return new Response().setMessage("登录成功！").addString("url", newUrl).toJson();
    }

    /**
     * 注销
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "logout", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("device");
        return new Response().setMessage("注销成功！").addString("url", "#").toJson();
    }

    @RequestMapping(value = "active",produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String active(String dataJson,String deviceJson,HttpServletRequest request){
        if(StringUtils.isEmpty(dataJson,deviceJson)) return Response.paramsIsEmpty("用户信息、设备信息");
        JSONObject data=JSONObject.fromObject(dataJson);
        User user =new User();
        user.setId(data.get("id").toString());
        user.setSex(data.get("sex").toString());
        user.setBirthday(data.get("birthday").toString());
        user.setBloodType(data.get("bloodType").toString());
        user.setMobilePhone(data.get("mobilePhone").toString());
        user.setEmail(data.get("email").toString());
        user.setActive("1");
        user.setStatus("1");
        if(userService.active(user)){
            JSONObject device =JSONObject.fromObject(deviceJson);
            Device dbDevice=new Device();
            dbDevice.setId(device.get("id").toString());
            dbDevice.setUserId(device.get("userId").toString());
            dbDevice.setName(device.get("name").toString());
            dbDevice.setBrand(device.get("brand").toString());
            dbDevice.setModel(device.get("model").toString());
            dbDevice.setOs(device.get("os").toString());
            dbDevice.setType(device.get("type").toString());
            dbDevice.setStatus(device.get("status").toString());
            deviceService.insertDevice(dbDevice);
            request.setAttribute("user",user);
            request.setAttribute("device",device);
            return new Response().addString("url","#").setMessage("激活成功！").toJson();
        }
        else return Response.failuer("激活失败！");
    }
}
