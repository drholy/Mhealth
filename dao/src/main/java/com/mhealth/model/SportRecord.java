package com.mhealth.model;

import com.mhealth.common.base.BaseEntity;

/**
 * Created by pengt on 2016.4.9.0009.
 */
public class SportRecord extends BaseEntity{

    private String userId;
    private String deviceId;
    private String sport_heartRate;
    private String distance;
    private String heat;
    private String step;
    private String elevation;
    private String beginTime;
    private String endTime;
    private String uploadTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSport_heartRate() {
        return sport_heartRate;
    }

    public void setSport_heartRate(String sport_heartRate) {
        this.sport_heartRate = sport_heartRate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
}
