package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.bean.UserVo;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hewei
 * @date 2019/9/29 - 14:27
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @RequestMapping("/index")
    public String index(){
        return "user/index";
    }

    @RequestMapping("/add")
    public String add(User user){
        return "user/add";
    }



    /*@RequestMapping("/index")
    public ModelAndView index(@RequestParam(value="page",required = false,defaultValue = "1") Integer page, @RequestParam(value="pagesize",required = false,defaultValue = "10")Integer pagesize){
        ModelAndView mv = new ModelAndView();
        PageInfo pageInfo = userService.queryUserPage(page,pagesize);
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("user/index-1");
        return mv;
    }*/



    //异步请求分页查询
    /*@ResponseBody
    @RequestMapping("/userlists")
    public AjaxResult index(@RequestParam(value="page",required = false,defaultValue = "1") Integer page, @RequestParam(value="pagesize",required = false,defaultValue = "10")Integer pagesize){
        AjaxResult result = new AjaxResult();
        try {
            PageInfo pageInfo = userService.queryUserPage(page,pagesize);
            result.setPageInfo(pageInfo);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("数据查询错误");
            e.printStackTrace();
        }
        return result;
    }*/

    //异步请求分页查询和条件查询分页2者合1了。
    @ResponseBody
    @RequestMapping("/userlists")
    public AjaxResult userlists(@RequestParam(value="page",required = false,defaultValue = "1") Integer page,
                              @RequestParam(value="pagesize",required = false,defaultValue = "10")Integer pagesize,
                              String queryText){
        AjaxResult result = new AjaxResult();
        try {
            Map<String,Object> map = new HashMap<>();
            map.put("page",page);
            map.put("pagesize", pagesize);
            map.put("queryText", queryText);



            PageInfo pageInfo = userService.queryUserPage(map);
            result.setPageInfo(pageInfo);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("数据查询错误");
            e.printStackTrace();
        }
        return result;
    }




    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(User user){
        AjaxResult result = new AjaxResult();

        if (StringUtils.isBlank(user.getLoginacct()) || StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getEmail())){
            return null;
        }
        try {
            int count = userService.saveUser(user);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("保存用户失败");
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(Integer id){
        ModelAndView mv = new ModelAndView();
        User user = userService.queryById(id);

        mv.addObject("user", user);
        mv.setViewName("user/update");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/doUpdate")
    public AjaxResult doUpdate(User user){
        AjaxResult result = new AjaxResult();
        try {
            userService.updateUser(user);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/doDelete")
    public AjaxResult doDelete(Integer id){
        AjaxResult result = new AjaxResult();
        try {
            userService.deleteUser(id);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }


    //接收一个参数名带多个值
    /*@ResponseBody
    @RequestMapping("/doDeleteBatch")
    public AjaxResult doDeleteBatch(String ids){
        AjaxResult result = new AjaxResult();
        try {
            userService.doDeleteBatch(ids);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }*/

    //接收多条数据
    @ResponseBody
    @RequestMapping("/doDeleteBatch")
    public AjaxResult doDeleteBatch(UserVo userVo){
        AjaxResult result = new AjaxResult();
        try {
            userService.doDeleteBatch(userVo);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }


    @RequestMapping("/assignRole")
    public ModelAndView  assignRole(Integer id){
        ModelAndView mv = new ModelAndView();

        List<Role> roles = userService.getAllRoles();
        List<Integer> roleIds = userService.getRolesIdsByUserId(id);

        List<Role> leftRoles = new ArrayList<>();   //未分配角色
        List<Role> rightRoles = new ArrayList<>();  //已分配角色
        for (Role role : roles) {
            if(roleIds.contains(role.getId())){
                rightRoles.add(role);
            }else{
                leftRoles.add(role);
            }
        }

        mv.addObject("leftRoles",leftRoles);
        mv.addObject("rightRoles",rightRoles);
        mv.setViewName("user/assignRole");

        return mv;

    }


    @RequestMapping("/updateUserRoles")
    public ResponseEntity<Void> updateUserRoles(String roleIds,Integer userId){
        userService.updateUserRoles(roleIds,userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping("/test")
    public String  test(ServletRequest request){
        //获取所有前缀为s_的name存入map中
        Map<String,Object> map = WebUtils.getParametersStartingWith(request, "s_");
        for (Map.Entry<String,Object> entry :map.entrySet()){
            System.out.println("key："+entry.getKey()+",value:"+entry.getValue());

        }
        return "redirect:/index.jsp";

    }


}
