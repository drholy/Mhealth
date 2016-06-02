package com.mhealth.openstack.jclouds;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import com.google.inject.Module;
import org.jclouds.ContextBuilder;
import org.jclouds.io.Payload;
import org.jclouds.io.payloads.InputStreamPayload;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.swift.v1.SwiftApi;
import org.jclouds.openstack.swift.v1.domain.SwiftObject;
import org.jclouds.openstack.swift.v1.features.ObjectApi;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pengt on 2016.5.27.0027.
 */
@Component("jCloudsSwift")
@Scope("prototype")
public class JCloudsSwift implements Closeable {

    private static final String CONTAINER_NAME = "userImgs";    //swift容器名
    private static final String PROVIDER = "openstack-swift";   //服务提供者
    private static final String IDENTITY = "admin:admin";   //租户名：用户名
    private static final String CREDENTIAL = "admin_user_password"; //服务用户密码
    private static final String ENDPOINT = "http://192.168.56.180:5000/v2.0/";  //认证节点

    private SwiftApi swiftApi;

    public JCloudsSwift() {
        Iterable<Module> modules = ImmutableSet.<Module>of(
                new SLF4JLoggingModule());

        swiftApi = ContextBuilder.newBuilder(PROVIDER)
                .endpoint(ENDPOINT)
                .credentials(IDENTITY, CREDENTIAL)
                .modules(modules)
                .buildApi(SwiftApi.class);
    }

    /**
     * 上传文件到swift
     *
     * @param objectName
     * @param in
     */
    public void uploadObject(String objectName, InputStream in) {
        ObjectApi objectApi = swiftApi.getObjectApi("RegionOne", CONTAINER_NAME);
        Payload payload = new InputStreamPayload(in);
        objectApi.put(objectName, payload);
    }

    /**
     * 下载对象
     *
     * @param objectName
     * @return
     * @throws IOException
     */
    public InputStream getObject(String objectName) throws IOException {
        ObjectApi objectApi = swiftApi.getObjectApi("RegionOne", CONTAINER_NAME);
        SwiftObject swiftObject = objectApi.get(objectName);
        return swiftObject.getPayload().openStream();
    }

    @Override
    public void close() throws IOException {
        Closeables.close(swiftApi, true);
    }
}
