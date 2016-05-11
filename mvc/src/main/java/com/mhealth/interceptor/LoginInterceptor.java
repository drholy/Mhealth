package com.mhealth.interceptor;

import com.mhealth.common.entity.Response;
import com.mhealth.common.util.StringUtils;
import com.mhealth.model.Token;
import com.mhealth.model.User;
import com.mhealth.service.TokenService;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by pengt on 2016.4.28.0028.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Resource(name = "tokenService")
    private TokenService tokenService;

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        if (StringUtils.isEmpty(httpServletRequest.getParameter("access_token"))) {//浏览器登录
            if (httpServletRequest.getSession().getAttribute("user") == null && httpServletRequest.getSession().getAttribute("doctor") == null) {
                httpServletResponse.getWriter().println(Response.error(Response.NOT_LOGIN, "请登录！"));
                return false;
            }
            return true;
        } else { //移动端登录
            Token token = tokenService.getTokenByAcc(httpServletRequest.getParameter("access_token"));
            if (token.getExpire() <= System.currentTimeMillis() || token == null) {
                httpServletResponse.getWriter().println(Response.error(Response.TOKEN_EXPIRED, "token过期！"));
                return false;
            }
            httpServletRequest.setAttribute("userId", token.getId());
            return true;
        }
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
