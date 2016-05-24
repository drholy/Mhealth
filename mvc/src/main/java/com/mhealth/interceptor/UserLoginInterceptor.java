package com.mhealth.interceptor;

import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.Token;
import com.mhealth.service.TokenService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Handler;

/**
 * Created by pengt on 2016.5.23.0023.
 */
public class UserLoginInterceptor implements HandlerInterceptor {

    @Resource(name = "tokenService")
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String access_token = httpServletRequest.getParameter("access_token");
        if (StringUtils.isEmpty(access_token)) { //浏览器登录
            if (httpServletRequest.getSession().getAttribute("user") == null) {
                httpServletResponse.getWriter().println(Response.error(Response.NOT_LOGIN, "未登录！"));
                return false;
            } else return true;
        } else {
            Token token = tokenService.getTokenByAcc(access_token);
            if (token == null) {
                httpServletResponse.getWriter().println(Response.error(Response.NOT_LOGIN, "重新登录！"));
                return false;
            }
            if (token.getExpire() <= System.currentTimeMillis()) {
                httpServletResponse.getWriter().println(Response.error(Response.TOKEN_EXPIRED, "token过期！"));
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
