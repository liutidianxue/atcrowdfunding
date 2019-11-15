package com.atguigu.atcrowdfunding.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hewei
 * @date 2019/10/4 - 20:21
 */

public class LoginInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*System.out.println("helloworld");
        return true;*/

        //为什么要用set集合。待会会根据用户拥有的权限来进行是否限制访问。而用户拥有多个角色，角色拥有多个权限，用list集合的话会导致那些路径字符串们会重复。
        //其实我数据库是直接一条sql语句（用了distinct）根据用户id拿到所有的用户权限。用list也可以，根据用户拿角色，再根据角色拿权限也应该可以（用distinct？）？
        //不过数据库的路径有很多null，所以还是用set把。

        Set<String> allowedPaths = new HashSet<>();
        allowedPaths.add("/index.htm");
        allowedPaths.add("/user/reg.do");
        allowedPaths.add("/user/reg.htm");
        allowedPaths.add("/login.htm");
        allowedPaths.add("/doLogin.do");
        allowedPaths.add("/logout.do");
        //验证码后面有个new Date().getTime();  需要单独拿出来判断
        allowedPaths.add("/getVerifyCode.do");



        String requestPath = request.getServletPath();
        if(allowedPaths.contains(requestPath)){
            return true;
        }

        if(request.getSession().getAttribute("user") != null){
            return true;
        }else{
            response.sendRedirect(request.getContextPath()+"/login.htm");
            return false;
        }



    }


}
