package com.mhealth.common.base;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Created by pengt on 2016.4.11.0011.
 */
public class BaseEntity implements Serializable {
    
    protected String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
