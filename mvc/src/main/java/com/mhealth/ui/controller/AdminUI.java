package com.mhealth.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pengt on 2016.5.18.0018.
 */
@Controller
@RequestMapping("admin")
public class AdminUI {

    @RequestMapping("login.ui")
    public String login() {
        return "admin/login";
    }

    @RequestMapping("examDoc.ui")
    public String examDoc() {
        return "admin/examDoc";
    }
}
