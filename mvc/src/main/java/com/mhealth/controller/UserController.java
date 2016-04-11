package com.mhealth.controller;

import com.mhealth.common.entity.Response;
import com.mhealth.common.util.PasswordUtils;
import com.mhealth.model.User;
import com.mhealth.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by pengt on 2016.4.6.0006.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Resource(name = "userService")
    UserService userService;

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
        String id = UUID.randomUUID().toString();

        User user = new User();
        user.setId(id);
        user.setLoginName(jsonObject.get("loginName").toString());
        user.setUsername(jsonObject.get("username").toString());
        try {
            user.setPassword(PasswordUtils.getEncryptedPwd((String) jsonObject.get("password")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (userService.insertUser(user).equals(id))
            return new Response().addString("loginName", user.getLoginName()).addString("id", user.getId()).setMessage("注册成功").toJson();
        else return Response.failuer("注册失败");
    }
}
