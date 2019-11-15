package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.ex.PermissionEx;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hewei
 * @date 2019/10/2 - 17:29
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/index")
    public String index(){
        return "permission/index";
    }


    @RequestMapping("/toAdd")
    public String toAdd() {
        return "permission/add";
    }

    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(Integer id) {
        ModelAndView mv = new ModelAndView();

        Permission permission = permissionService.queryBuId(id);
        mv.addObject("permission",permission);
        mv.setViewName("permission/update");

        return mv;
    }


    @RequestMapping("/doAdd")
    public ResponseEntity<Void> doAdd(Permission permission) {
        permissionService.add(permission);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping("/doUpdate")
    public ResponseEntity<Void> doUpdate(Permission permission) {
        permissionService.update(permission);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping("/deletePermission")
    public ResponseEntity<Void> deletePermission(Integer id) {
        permissionService.delete(id);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }




    //前台是数组对象接收的数据，所以后台必须以list的形式传过去。
    //demo1 模拟数据生成
    //@RequestMapping("/showPermissions")
    /*public ResponseEntity<List<PermissionEx>> showPermissions(){

        List<PermissionEx> permissionExes = new ArrayList<>();

        PermissionEx permissionEx = new PermissionEx();
        permissionEx.setId(1);
        permissionEx.setName("123");
        permissionEx.setOpen(true);
        PermissionEx p1 = new PermissionEx();
        p1.setName("111");
        PermissionEx p2 = new PermissionEx();
        p2.setName("222");
        PermissionEx p3 = new PermissionEx();
        p3.setName("333");
        permissionEx.getChildren().add(p1);
        permissionEx.getChildren().add(p2);
        permissionEx.getChildren().add(p3);

        permissionExes.add(permissionEx);


        return ResponseEntity.ok(permissionExes);
    }*/

    //demo2 循环
    /*@RequestMapping("/showPermissions")
    public ResponseEntity<List<PermissionEx>> showPermissions(){
        //定义一代，二代，三代和将来要返回给浏览器的值
        PermissionEx rootEx = null;
        List<PermissionEx> root2Exs = null;
        List<PermissionEx> root3Exs = null;
        List<PermissionEx> permissionExes = new ArrayList<>();

        //根据id=null得到一代
        Permission root = permissionService.getRootpermission();
        rootEx = new PermissionEx();
        BeanUtils.copyProperties(root,rootEx);
        rootEx.setOpen(true);

        //根据  二代的pid=一代的id   得到二代(之所以这么麻烦，是因为我搞了个继承类，new出继承类对象，copy旧值，添加新值)
        List<Permission> root2 = permissionService.getChildren(rootEx.getId());
        for (Permission permission : root2) {
            PermissionEx root2Ex = new PermissionEx();
            BeanUtils.copyProperties(permission,root2Ex);
            //二代加入一代
            rootEx.getChildren().add(root2Ex);
        }

        root2Exs = rootEx.getChildren();

        //根据  三代的pid=二代的id  得到三代
        for (PermissionEx permissionEx : root2Exs) {
            List<Permission> root3 = permissionService.getChildren(permissionEx.getId());
            for (Permission permission : root3) {
                PermissionEx root3Ex = new PermissionEx();
                BeanUtils.copyProperties(permission,root3Ex);
                //三代加入二代
                permissionEx.getChildren().add(root3Ex);
            }

        }

        permissionExes.add(rootEx);

        return ResponseEntity.ok(permissionExes);
    }*/




    //demo5 只访问一次数据库
   /* @RequestMapping("/showPermissions")
    public ResponseEntity<List<PermissionEx>> showPermissions(){
        //定义一个待会要返回给前台的集合
        List<PermissionEx> permissionExes = new ArrayList<>();

        //查询所有权限
        List<Permission> allpermissions = permissionService.getAll();

        //List<PermissionEx> root = new ArrayList<>();

        //定义根权限(这个是老大，就他一个，所以没定义集合。如果我这里定义一个集合，是不是只传这个老大就行了啊)
        PermissionEx root = null;

        //定义二代
        List<PermissionEx> root2 = null;
        for (Permission permission : allpermissions) {
            if(permission.getPid() ==null){
                PermissionEx permissionEx = new PermissionEx();
                permissionEx.setId(permission.getId());
                permissionEx.setName(permission.getName());
                permissionEx.setOpen(true);

                //得到了根权限
                root = permissionEx;

            }else{
                if(permission.getPid() == root.getId()){
                    PermissionEx permissionEx = new PermissionEx();
                    permissionEx.setId(permission.getId());
                    permissionEx.setName(permission.getName());
                    //满足条件的二代加入一代的后代
                    root.getChildren().add(permissionEx);
                }
            }
        }
        //给二代赋值
        root2 = root.getChildren();

        for (Permission permission : allpermissions) {
            for (PermissionEx permissionEx : root2) {
                //permissionEx每一个二代
                if(permission.getPid() == permissionEx.getId()){
                    //permissionEx2是为了不跟上面的重复
                    PermissionEx permissionEx2 = new PermissionEx();
                    permissionEx2.setId(permission.getId());
                    permissionEx2.setName(permission.getName());
                    //满足条件的三代加入二代的后代
                    permissionEx.getChildren().add(permissionEx2);
                }
            }
        }

        permissionExes.add(root);



        return ResponseEntity.ok(permissionExes);
    }*/


    //demoTest 循环
    @RequestMapping("/showPermissions")
    public ResponseEntity<List<PermissionEx>> showPermissions(){

        List<PermissionEx> result = permissionService.getAllEx();

        return ResponseEntity.ok(result);
    }
}
