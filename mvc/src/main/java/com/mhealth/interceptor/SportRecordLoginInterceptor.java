package com.mhealth.interceptor;

import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.Admin;
import com.mhealth.model.Doctor;
import com.mhealth.model.Token;
import com.mhealth.model.User;
import com.mhealth.service.TokenService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by pengt on 2016.5.23.0023.
 */
public class SportRecordLoginInterceptor implements HandlerInterceptor {

    @Resource(name = "tokenService")
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String access_token = httpServletRequest.getParameter("access_token");
        if (StringUtils.isEmpty(access_token)) { //浏览器登录
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            Doctor doctor = (Doctor) httpServletRequest.getSession().getAttribute("doctor");
            Admin admin = (Admin) httpServletRequest.getSession().getAttribute("admin");
            String userId = httpServletRequest.getParameter("userId");
            if (user == null && doctor == null && admin == null) {
                httpServletResponse.getWriter().println(Response.error(Response.NOT_LOGIN, "未登录！"));
                return false;
            } else if (user != null && doctor == null) { //user用户
                if (!StringUtils.isEmpty(userId)) {
                    if (!userId.equals(user.getId())) {
                        httpServletResponse.getWriter().println(Response.failuer("您没有查询其他用户的权限！"));
                        return false;
                    } else return true;
                }
                return true;
            } else if (doctor != null && user == null) { //doctor用户
                if (!StringUtils.isEmpty(userId)) {
                    for (Map<String, Object> map : doctor.getUserList()) {
                        if (map.get("id").equals(userId)) return true;
                    }
                    httpServletResponse.getWriter().println(Response.failuer("您没有查询其他用户的权限！"));
                    return false;
                } else return true;
            } else return true;
        } else {  //移动端登录
            Token token = tokenService.getTokenByAcc(access_token);
            if (token == null) {
                httpServletResponse.getWriter().println(Response.error(Response.NOT_LOGIN, "重新登录！"));
                return false;
            }
            if (token.getExpire() <= System.currentTimeMillis()) {
                httpServletResponse.getWriter().println(Response.error(Response.TOKEN_EXPIRED, "token过期！"));
                return false;
            }
            String userId = httpServletRequest.getParameter("userId");
            if (!StringUtils.isEmpty(userId)) {
                if (!token.getUserId().equals(userId)) {
                    httpServletResponse.getWriter().println(Response.failuer("您没有查询其他用户的权限！"));
                    return false;
                }
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
