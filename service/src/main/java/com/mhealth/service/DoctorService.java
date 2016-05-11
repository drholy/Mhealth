package com.mhealth.service;

import com.mhealth.common.entity.QuickPager;
import com.mhealth.model.Doctor;
import com.mhealth.repository.DoctorDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by pengt on 2016.5.9.0009.
 */
@Service("doctorService")
public class DoctorService {

    @Resource(name = "doctorDao")
    private DoctorDao doctorDao;

    /**
     * 医生注册
     *
     * @param doctor
     */
    public String insertDoc(Doctor doctor) {
        return doctorDao.insertDoc(doctor);
    }

    /**
     * 分页返回所有医生
     *
     * @param quickPager
     */
    public void getAllDoc(QuickPager<Doctor> quickPager) {
        doctorDao.getAllDoc(quickPager);
    }

    /**
     * 检查loginName
     *
     * @param loginName
     * @return
     */
    public boolean checkLoginName(String loginName) {
        return doctorDao.checkLoginName(loginName);
    }

    /**
     * 根据loginName返回医生
     *
     * @param loginName
     * @return
     */
    public Doctor getDocByLogin(String loginName) {
        return doctorDao.getDocByLogin(loginName);
    }

    /**
     * 根据医生id查询医生
     *
     * @param id
     * @return
     */
    public Doctor getDocById(String id) {
        return doctorDao.getDocById(id);
    }

    /**
     * 选择医生
     *
     * @param doctorId
     * @param userMap
     * @return
     */
    public boolean chooseDoc(String doctorId, Map<String, Object> userMap) {
        return doctorDao.chooseDoc(doctorId, userMap);
    }

    /**
     * 取消医生
     *
     * @param userId
     * @param doctorId
     * @return
     */
    public boolean cancelDoc(String userId, String doctorId) {
        return doctorDao.cancelDoc(userId, doctorId);
    }

    /**
     * 根据用户返回医生
     *
     * @param userId
     * @return
     */
    public Doctor getDocByUser(String userId) {
        return doctorDao.getDocByUser(userId);
    }

    /**
     * 修改医生密码
     *
     * @param doctor
     * @return
     */
    public boolean changePasswd(Doctor doctor) {
        return doctorDao.changePasswd(doctor);
    }

    /**
     * 修改资料
     *
     * @param doctor
     * @return
     */
    public boolean modify(Doctor doctor) {
        return doctorDao.modify(doctor);
    }
}
