package com.atguigu.atcrowdfunding.interceptor;

import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author hewei
 * @date 2019/10/5 - 0:03
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Set<String> allURIs = (Set<String>)request.getSession().getServletContext().getAttribute("allUrls");


        //2.判断请求路径是否在所有许可范围内
        String requestPath = request.getServletPath();
        if(allURIs.contains(requestPath)){

            //3.判断请求路径是否在用户所拥有的权限内
            Set<String> urlsOfUser = (Set<String>)request.getSession().getAttribute("urlsOfUser");
            if(urlsOfUser.contains(requestPath)){
                return true ;
            }else{
                response.sendRedirect(request.getContextPath()+"/login.htm");
                return false ;
            }

        }
        return true ;



    }
}
