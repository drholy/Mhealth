package com.mhealth.interceptor;

import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.Doctor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by pengt on 2016.5.23.0023.
 */
public class DoctorLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        Doctor doctor = (Doctor) httpServletRequest.getSession().getAttribute("doctor");
        String doctorId = httpServletRequest.getParameter("doctorId");

        if (doctor == null) {
            httpServletResponse.getWriter().println(Response.error(Response.NOT_LOGIN, "未登录！"));
            return false;
        } else {
            if (!StringUtils.isEmpty(doctorId) && !doctor.getId().equals(doctorId)) {
                httpServletResponse.getWriter().println(Response.failuer("您没有操作其他医生的权限！"));
                return false;
            }
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
