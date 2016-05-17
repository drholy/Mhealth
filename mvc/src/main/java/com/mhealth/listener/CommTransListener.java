package com.mhealth.listener;

import com.mhealth.service.DoctorService;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by pengt on 2016.5.17.0017.
 */
public class CommTransListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        Runnable rollback = new Runnable() {
            @Resource(name = "doctorService")
            private DoctorService doctorService;

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
