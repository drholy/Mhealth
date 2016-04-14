package com.mhealth.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pengt on 2016.4.13.0013.
 */
@Controller
@RequestMapping("record")
public class SportRecordUI {

    @RequestMapping("overview.ui")
    public String overview(){
        return "sportRecord/recordOverview";
    }
}
