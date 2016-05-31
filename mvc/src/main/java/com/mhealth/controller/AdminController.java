package com.mhealth.controller;

import com.mhealth.common.entity.QuickPager;
import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.Admin;
import com.mhealth.model.Doctor;
import com.mhealth.model.User;
import com.mhealth.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by pengt on 2016.5.18.0018.
 */
@Controller
@RequestMapping("adminData")
public class AdminController {

    @Resource(name = "adminService")
    private AdminService adminService;

    /**
     * 管理员登录
     *
     * @param loginName
     * @param password
     * @param request
     * @return
     */
    @RequestMapping(value = "login", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String login(String loginName, String password, HttpServletRequest request) {
        if (StringUtils.isEmpty(loginName)) return Response.paramsIsEmpty("loginName");
        Admin admin = adminService.getAdminByLogin(loginName);
        if (!password.equals(admin.getPassword())) return Response.failuer("密码错误！");
        request.getSession().setAttribute("admin", admin);
        return Response.success("登录成功！");
    }

    /**
     * 管理员注销
     *
     * @param adminId
     * @param request
     * @return
     */
    @RequestMapping(value = "logout", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("admin");
        return Response.success("注销成功！");
    }

    /**
     * 分页返回未激活医生
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getApplyDoc", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getApplyDoc(String currPage, String pageSize) {
        QuickPager<Doctor> quickPager = new QuickPager<>(currPage, pageSize);
        adminService.getApplyDoc(quickPager);
        return new Response().toPageJson(quickPager);
    }

    /**
     * 激活医生
     *
     * @param doctorId
     * @return
     */
    @RequestMapping(value = "activeDoc", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String activeDoc(String doctorId) {
        if (StringUtils.isEmpty(doctorId)) return Response.paramsIsEmpty("doctorId");
        if (adminService.activeDoc(doctorId)) return Response.success("激活成功！");
        else return Response.failuer("数据库错误！");
    }

    /**
     * 删除未通过医生
     *
     * @param doctorId
     * @return
     */
    @RequestMapping(value = "delDoc", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String delDoc(String doctorId) {
        if (StringUtils.isEmpty(doctorId)) return Response.paramsIsEmpty("doctorId");
        if (adminService.delDoc(doctorId)) return Response.success("删除成功！");
        else return Response.failuer("数据库错误！");
    }

    /**
     * 根据条件分页返回用户
     *
     * @param currPage
     * @param pageSize
     * @param detail
     * @return
     */
    @RequestMapping(value = "getAllUsers", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllUsers(String currPage, String pageSize, String detail) {
        QuickPager<User> quickPager = new QuickPager<>(currPage, pageSize);
        adminService.getAllUsers(quickPager, detail);
        return new Response().toPageJson(quickPager);
    }

    /**
     * 根据条件分页返回合法医生
     *
     * @param currPage
     * @param pageSize
     * @param detail
     * @return
     */
    @RequestMapping(value = "getAllDoc", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAllDoc(String currPage, String pageSize, String detail) {
        QuickPager<Doctor> quickPager = new QuickPager<>(currPage, pageSize);
        adminService.getAllDoc(quickPager, detail);
        return new Response().toPageJson(quickPager);
    }
}
