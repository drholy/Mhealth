package com.mhealth.model;


import com.mhealth.common.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pengt on 2016.5.9.0009.
 */
public class Doctor extends BaseEntity {

    private String loginName;
    private String password;
    private String realName;
    private String organization;
    private String office;
    private String certificate;
    private String headImg;
    private String mobilePhone;
    private String email;
    private String active;
    private String status;
    private long regTime;
    private List<Map<String, Object>> userList = new ArrayList<>();
    private List<String> pendingTransactions = new ArrayList<>();

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public List<Map<String, Object>> getUserList() {
        return userList;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserList(List<Map<String, Object>> userList) {

        this.userList = userList;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public long getRegTime() {
        return regTime;
    }

    public void setRegTime(long regTime) {
        this.regTime = regTime;
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

    public List<String> getPendingTransactions() {
        return pendingTransactions;
    }

    public void setPendingTransactions(List<String> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }
}
