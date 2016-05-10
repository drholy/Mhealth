package com.mhealth.model;

import com.mhealth.common.base.BaseEntity;

/**
 * Created by pengt on 2016.5.10.0010.
 */
public class Comment extends BaseEntity {

    private String doctorId;
    private String docRealName;
    private String title;
    private String content;
    private long time;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDocRealName() {
        return docRealName;
    }

    public void setDocRealName(String docRealName) {
        this.docRealName = docRealName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
