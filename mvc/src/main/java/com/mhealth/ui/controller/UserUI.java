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
    public String login(){
        return "user/login";
    }
}
