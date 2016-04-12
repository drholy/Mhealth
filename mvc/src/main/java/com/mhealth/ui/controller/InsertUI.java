package com.mhealth.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pengt on 2016.4.12.0012.
 */
@Controller
@RequestMapping("temp")
public class InsertUI {

    @RequestMapping("insert.ui")
    public String insertView(){
        return "insertRecord";
    }
}
