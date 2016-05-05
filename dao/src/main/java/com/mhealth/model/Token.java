package com.mhealth.model;

import com.mhealth.common.base.BaseEntity;

/**
 * Created by pengt on 2016.5.5.0005.
 */
public class Token extends BaseEntity {

    private String userId;
    private String access_token;
    private long expire;

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
