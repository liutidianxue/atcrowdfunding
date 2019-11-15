package com.atcrowdfunding;

import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.util.MD5Util;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author hewei
 * @date 2019/9/29 - 16:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-*.xml")
public class test {

    @Autowired
    private UserService userService;

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 数据库用户表添加100条数据
     */
    @Test
    public void teadAddUser(){
        for (int i = 1; i <= 100; i++) {
            User user = new User();
            user.setLoginacct("test"+i);
            user.setUserpswd(MD5Util.digest("123"));
            user.setUsername("test"+i*10);
            user.setEmail("test"+i+"@qq.com");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createtime = sdf.format(new Date());
            user.setCreatetime(createtime);
            userService.saveUser(user);

        }
    }

    @Test
    public void test01(){
        System.out.println(processEngine);
    }



}
