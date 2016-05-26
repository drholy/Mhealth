package com.mhealth.interceptor;

import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.Token;
import com.mhealth.model.User;
import com.mhealth.service.TokenService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        String userId = httpServletRequest.getParameter("userId");

        if (StringUtils.isEmpty(access_token)) { //浏览器登录
            if (user == null) {
                httpServletResponse.getWriter().println(Response.error(Response.NOT_LOGIN, "未登录！"));
                return false;
            } else {
                if (!StringUtils.isEmpty(userId) && !user.getId().equals(userId)) {
                    httpServletResponse.getWriter().println(Response.failuer("您没有操作其他用户的权限！"));
                    return false;
                }
                return true;
            }
        } else { //移动端登录
            Token token = tokenService.getTokenByAcc(access_token);
            if (token == null) {
                httpServletResponse.getWriter().println(Response.error(Response.NOT_LOGIN, "重新登录！"));
                return false;
            }
            if (token.getExpire() <= System.currentTimeMillis()) {
                httpServletResponse.getWriter().println(Response.error(Response.TOKEN_EXPIRED, "token过期！"));
                return false;
            }
            if (!StringUtils.isEmpty(userId) && !token.getUserId().equals(userId)) {
                httpServletResponse.getWriter().println(Response.failuer("您没有操作其他用户的权限！"));
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
