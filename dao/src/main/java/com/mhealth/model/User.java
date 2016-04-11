package com.mhealth.model;

import com.mhealth.common.base.BaseEntity;

/**
 * Created by pengt on 2016.4.9.0009.
 */
public class User extends BaseEntity{

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    private String loginName;
    private String username;
    private String password;
    private String sex;
    private String birthday;
    private String bloodType;
    private String mobilePhone;
    private String email;
    private String userType;
    private String active;
    private String regTime;
    private String status;

    public User() {
    }

    public User(String id,String loginName, String username, String password, String sex, String birthday, String bloodType, String mobilePhone, String email, String userType, String active, String regTime, String status) {
        this.id = id;
        this.loginName=loginName;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.birthday = birthday;
        this.bloodType = bloodType;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.userType = userType;
        this.active = active;
        this.regTime = regTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
