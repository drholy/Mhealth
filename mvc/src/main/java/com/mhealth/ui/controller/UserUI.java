package com.mhealth.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pengt on 2016.4.26.0026.
 */
@Controller
@RequestMapping("user")
public class UserUI {

    @RequestMapping("login.ui")
    public String login() {
        return "user/login";
    }

    @RequestMapping("active.ui")
    public String active() {
        return "user/active";
    }

    @RequestMapping("modify.ui")
    public String modify() {
        return "user/modify";
    }

    @RequestMapping("profile.ui")
    public String profile() {
        return "user/profile";
    }

    @RequestMapping("changePasswd.ui")
    public String changePasswd() {
        return "user/changePasswd";
    }

    @RequestMapping("chooseDoctor.ui")
    public String chooseDoctor() {
        return "user/chooseDoctor";
    }

    @RequestMapping("myDoctor.ui")
    public String myDoctor() {
        return "user/myDoctor";
    }

    @RequestMapping("comment.ui")
    public String myComment() {
        return "user/comment";
    }
}
