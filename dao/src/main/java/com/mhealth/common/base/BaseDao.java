package com.mhealth.common.base;

import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;

/**
 * Created by pengt on 2016.4.11.0011.
 */
public class BaseDao {
    @Resource(name = "mongoTemplate")
    protected MongoTemplate mongoTemplate;
}
