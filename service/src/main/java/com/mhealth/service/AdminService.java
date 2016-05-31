package com.mhealth.service;

import com.mhealth.common.entity.QuickPager;
import com.mhealth.model.Admin;
import com.mhealth.model.Doctor;
import com.mhealth.model.User;
import com.mhealth.repository.AdminDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by pengt on 2016.5.18.0018.
 */
@Service("adminService")
public class AdminService {

    @Resource(name = "adminDao")
    private AdminDao adminDao;

    /**
     * 根据登录名返回管理员
     *
     * @param loginName
     * @return
     */
    public Admin getAdminByLogin(String loginName) {
        return adminDao.getAdminByLogin(loginName);
    }

    /**
     * 根据id返回管理员
     *
     * @param adminId
     * @return
     */
    public Admin getAdminById(String adminId) {
        return adminDao.getAdminById(adminId);
    }

    /**
     * 分页返回未激活的医生
     *
     * @param quickPager
     */
    public void getApplyDoc(QuickPager<Doctor> quickPager) {
        adminDao.getApplyDoc(quickPager);
    }

    /**
     * 激活医生
     *
     * @param doctorId
     * @return
     */
    public boolean activeDoc(String doctorId) {
        return adminDao.activeDoc(doctorId);
    }

    /**
     * 删除未通过医生
     *
     * @param doctorId
     * @return
     */
    public boolean delDoc(String doctorId) {
        return adminDao.delDoc(doctorId);
    }

    /**
     * 根据条件分页返回所有用户
     *
     * @param quickPager
     * @param detail
     */
    public void getAllUsers(QuickPager<User> quickPager, String detail) {
        adminDao.getAllUsers(quickPager, detail);
    }

    /**
     * 根据条件返回合法医生
     *
     * @param quickPager
     * @param detail
     */
    public void getAllDoc(QuickPager<Doctor> quickPager, String detail) {
        adminDao.getAllDoc(quickPager, detail);
    }
}
