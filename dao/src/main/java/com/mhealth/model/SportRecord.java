package com.mhealth.model;

import com.mhealth.common.base.BaseEntity;

/**
 * Created by pengt on 2016.4.9.0009.
 */
public class SportRecord extends BaseEntity{

    private String userId;
    private String deviceId;
    private long sport_heartRate;   //心率
    private long distance;  //距离
    private long stepCount; //步数
    private long elevation; //海拔
    private long beginTime;
    private long endTime;
    private long uploadTime;

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

    public long getSport_heartRate() {
        return sport_heartRate;
    }

    public void setSport_heartRate(long sport_heartRate) {
        this.sport_heartRate = sport_heartRate;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public long getStepCount() {
        return stepCount;
    }

    public void setStepCount(long stepCount) {
        this.stepCount = stepCount;
    }

    public long getElevation() {
        return elevation;
    }

    public void setElevation(long elevation) {
        this.elevation = elevation;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }
}
