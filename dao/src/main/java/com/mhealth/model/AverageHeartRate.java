package com.mhealth.model;

/**
 * Created by pengt on 2016.4.14.0014.
 */
public class AverageHeartRate {
    String userId;
    long average;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getAverage() {
        return average;
    }

    public void setAverage(long average) {
        this.average = average;
    }
}
