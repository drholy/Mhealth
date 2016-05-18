package com.mhealth.listener;

import com.mhealth.service.DoctorService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by pengt on 2016.5.17.0017.
 */
public class CommTransListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
        final DoctorService doctorService = (DoctorService) wac.getBean("doctorService");

        Runnable rollback = new Runnable() {
//            @Resource(name = "doctorService")
//            private DoctorService doctorService;

            @Override
            public void run() {
                while (true) {
                    try {
                        doctorService.pendingRecovery();
                        doctorService.appliedRecovery();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(rollback).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
