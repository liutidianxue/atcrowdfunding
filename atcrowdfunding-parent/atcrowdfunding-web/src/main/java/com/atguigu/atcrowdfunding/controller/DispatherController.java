package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.exception.LoginFailException;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.atguigu.atcrowdfunding.util.MD5Util;
import com.atguigu.atcrowdfunding.util.RandomValidateCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author hewei
 * @date 2019/9/28 - 1:58
 */
//aaaaa
@Controller
public class DispatherController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }


    @RequestMapping("/login")
    public String login() {
        return "login";
    }


    @ResponseBody
    @RequestMapping("/doLogin")
    public Object doLogin(String loginacct, String userpswd, String type, String verifyCode, HttpSession session) throws Exception {
        AjaxResult result = new AjaxResult();
        //首先判断验证码
        //他的工具类已经把生成的验证码存入session中了，直接拿就好了
        String verifyCode2 = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
        if (StringUtils.isBlank(verifyCode) || StringUtils.isBlank(verifyCode2) || !verifyCode.toUpperCase().equals(verifyCode2.toUpperCase())) {
            result.setSuccess(false);
            result.setMessage("请输入正确的验证码");
            return result;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("loginacct", loginacct);
            paramMap.put("userpswd", MD5Util.digest(userpswd));
            paramMap.put("type", type);

            User user = userService.queryUserlogin(paramMap);

            session.setAttribute("user", user);
            result.setSuccess(true);

            Set<String> urlsOfUser = permissionService.getUrlsOfUser(user.getId());

            session.setAttribute("urlsOfUser",urlsOfUser);



        } catch (LoginFailException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("用户名或者密码错误");
        }
        return result;
    }

    //@RequestMapping("/doLogin")
    /*public String doLogin(String loginacct, String userpswd, HttpSession session) throws LoginFailException {
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("loginacct",loginacct);
        paramMap.put("userpswd",userpswd);
        User user = userService.queryUserlogin(paramMap);
        session.setAttribute("user", user);
        return "redirect:/main.htm";
    }*/

    @RequestMapping("/getVerifyCode")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        RandomValidateCode randomValidateCode = new RandomValidateCode();
        try {
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
            String verifyCode = (String)request.getSession().getAttribute("RANDOMVALIDATECODEKEY");
            System.out.println("验证码是"+verifyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        //有session，并且session有user属性就移除user属性。
        if (request.getSession(false) != null && request.getSession().getAttribute("user") != null) {
            request.getSession().invalidate();
        }

        return "redirect:/index.htm";
    }


    /**
     * 角色能查看的功能菜单。
     * @param session
     * @return
     */
    @RequestMapping("/main")
    public String main(HttpSession session) {

        User user = (User) session.getAttribute("user");

        Permission permissionRoot = userService.getPermissionRootByUserId(user.getId());

        session.setAttribute("permissionRoot", permissionRoot);

        return "main";
    }

}
