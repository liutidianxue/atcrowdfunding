package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.ex.Data;
import com.atguigu.atcrowdfunding.bean.ex.PermissionEx;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.manager.service.RoleService;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author hewei
 * @date 2019/10/1 - 16:07
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;
    
    @RequestMapping("index")
    public String index(){
        return "role/index";
    }
    
    @RequestMapping("/add")
    public String add(Role role){
        return "role/add";
    }



    @RequestMapping("/assignPermission")
    public String assignPermission(){
        return "role/assignPermission";
    }


    /*@RequestMapping("/loadDataAsync")
    public ResponseEntity<List<PermissionEx>> loadDataAsync(Integer id){
        //定义一代和将来要返回给浏览器的值
        List<PermissionEx> permissionExes = new ArrayList<>();

        //根据id=null得到一代
        Permission root = permissionService.getRootpermission();
        PermissionEx rootEx = new PermissionEx();
        BeanUtils.copyProperties(root,rootEx);
        rootEx.setOpen(true);

        //根据一代得到二代
        List<PermissionEx> root2Exs = permissionService.getChildren(rootEx.getId());
        rootEx.setChildrenEx(root2Exs);

        //根据一代得到二代(因为一代就他一个，所以上面没有用遍历)
        for (PermissionEx permissionEx : root2Exs) {
            List<PermissionEx> root3Exs = permissionService.getChildren(permissionEx.getId());
            permissionEx.setChildrenEx(root3Exs);
        }

        //拿到所有数据了
        permissionExes.add(rootEx);




        //没办法，Ztree这个插件就是要你返回list集合。不知道传数组和set集合行不行
        return ResponseEntity.ok(permissionExes);
    }*/



    //异步请求分页查询和条件查询分页2者合1了。
    @ResponseBody
    @RequestMapping("/rolelists")
    public AjaxResult rolelists(@RequestParam(value="page",required = false,defaultValue = "1") Integer page,
                                @RequestParam(value="pagesize",required = false,defaultValue = "10")Integer pagesize,
                                String queryText){
        AjaxResult result = new AjaxResult();
        try {
            Map<String,Object> map = new HashMap<>();
            map.put("page",page);
            map.put("pagesize", pagesize);
            map.put("queryText", queryText);



            PageInfo pageInfo = roleService.queryRolePage(map);
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
    public Object doAdd(Role role){
        AjaxResult result = new AjaxResult();

        if (StringUtils.isBlank(role.getName())){
            return null;
        }
        try {
            int count = roleService.saveRole(role);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("保存角色失败");
            e.printStackTrace();
        }
        return result;
    }


    //到修改用户页面，并且回显数据
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(Integer id){
        ModelAndView mv = new ModelAndView();
        Role role = roleService.queryById(id);

        mv.addObject("role", role);

        mv.setViewName("role/update");
        return mv;
    }


    @ResponseBody
    @RequestMapping("/doUpdate")
    public AjaxResult doUpdate(Role role){
        AjaxResult result = new AjaxResult();
        try {
            roleService.updateRole(role);
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
            roleService.deleteRole(id);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }




    //接收一个参数名带多个值
    @ResponseBody
    @RequestMapping("/doDeleteBatch")
    public AjaxResult doDeleteBatch(String ids){
        AjaxResult result = new AjaxResult();
        try {
            boolean success = roleService.doDeleteBatch(ids);
            result.setSuccess(success);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 查询所有的Permission并把角色拥有的权限check属性设置为true,然后转化为PermissionEx并返回
     * @param id roleId
     * @return
     */
    @RequestMapping("/loadDataAsync")
    public ResponseEntity<List<PermissionEx>> loadDataAsync(Integer id){

        List<PermissionEx> result = permissionService.getAllEx(id);

        return ResponseEntity.ok(result);
    }


    @RequestMapping("/doAssignPermission")
    public ResponseEntity<Void> doAssignPermission(Integer roleId, Data data){

        permissionService.saveRolePermissionRelationship(roleId,data.getIds());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
