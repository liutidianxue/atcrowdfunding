package com.atguigu.atcrowdfunding.listener;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.manager.service.impl.PermissionServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hewei
 * @date 2019/9/28 - 1:30
 */
public class StartSystemListener implements ServletContextListener {
//    在服务器启动时，创建application对象是需要执行的方法
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //视频喜欢把服务器一加载就放进application域中的属性，专门用一个类来定义这些值为常量
        //把前台的${pageContext.request.contextPah}简化为${AP_PATH}
        ServletContext application = servletContextEvent.getServletContext();
        application.setAttribute("APP_PATH", application.getContextPath());

        //因为用户权限访问的问题。不可能用户每访问一个路径，查询所有路径跟用户本身的路径做比较来控制访问权限，这样对数据库的压力较大，直接服务器一启动就放在application域中。
        ApplicationContext ioc = WebApplicationContextUtils.getWebApplicationContext(application);
        PermissionService permissionService = ioc.getBean(PermissionService.class);
        List<Permission> permissions = permissionService.getAll();

        Set<String> allUrls = permissions.stream().map(permission -> {
            String url = "/" + permission.getUrl();
            return url;
        }).collect(Collectors.toSet());
        application.setAttribute("allUrls", allUrls);


    }

    
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
