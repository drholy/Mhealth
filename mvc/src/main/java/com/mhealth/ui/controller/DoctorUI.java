package com.mhealth.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pengt on 2016.5.10.0010.
 */
@Controller
@RequestMapping("doctor")
public class DoctorUI {

    @RequestMapping("register.ui")
    public String register() {
        return "doctor/register";
    }

    @RequestMapping("login.ui")
    public String login() {
        return "doctor/login";
    }

    @RequestMapping("getUsers.ui")
    public String getUsers() {
        return "doctor/usersOfDoctor";
    }

    @RequestMapping("profile.ui")
    public String profile() {
        return "doctor/profile";
    }

    @RequestMapping("modify.ui")
    public String modify() {
        return "doctor/modify";
    }

    @RequestMapping("changePasswd.ui")
    public String changePasswd() {
        return "doctor/changePasswd";
    }

    @RequestMapping("comment.ui")
    public String comment(String userId, ModelMap modelMap) {
        modelMap.put("userId",userId);
        return "doctor/comment";
    }

    @RequestMapping("regSuccess.ui")
    public String regSuccess(String loginName, ModelMap modelMap) {
        modelMap.put("loginName", loginName);
        return "doctor/regSuccess";
    }
}
