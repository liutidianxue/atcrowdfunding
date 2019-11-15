package com.atguigu.atcrowdfunding.manager.service.impl;

import com.atguigu.atcrowdfunding.bean.*;
import com.atguigu.atcrowdfunding.bean.ex.PermissionEx;
import com.atguigu.atcrowdfunding.exception.LoginFailException;
import com.atguigu.atcrowdfunding.manager.mapper.PermissionMapper;
import com.atguigu.atcrowdfunding.manager.mapper.RoleMapper;
import com.atguigu.atcrowdfunding.manager.mapper.UserMapper;
import com.atguigu.atcrowdfunding.manager.mapper.UserRoleMapper;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.util.MD5Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hewei
 * @date 2019/9/28 - 2:27
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public User queryUserlogin(Map<String, String> paramMap) throws LoginFailException {
        User user =  userMapper.queryUserlogin(paramMap);
        if (user == null) {
            throw new LoginFailException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public PageInfo queryUserPage(Map map) {
        String queryText = (String)map.get("queryText");
        if(!StringUtils.isBlank(queryText)){
            if (queryText.contains("%")){
                queryText = queryText.replaceAll("%","\\\\%");
            }
            queryText = "%"+queryText+"%";
            map.put("queryText",queryText);
        }
        PageHelper.startPage((Integer) map.get("page"),(Integer)map.get("pagesize"));
        List<User> users = userMapper.selectAllByLoginacct(map);    //非条件查询和根据名字查询合体了，实在不知道取什么名字，就这样吧。
        PageInfo pageInfo = new PageInfo(users);
        return pageInfo;
    }

    @Override
    public int saveUser(User user) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(new Date());

        user.setUserpswd(MD5Util.digest("123"));
        user.setCreatetime(createTime);


        return userMapper.insert(user);
    }

    @Override
    public User queryById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void doDeleteBatch(UserVo userVo) {

        userMapper.DeleteBatchByVo(userVo);

    }

    @Override
    public List<Role> getAllRoles() {
        return roleMapper.selectAll();
    }

    @Override
    public List<Integer> getRolesIdsByUserId(Integer id) {
        return userRoleMapper.getRolesIdsByUserId(id);
    }

    @Override
    public void updateUserRoles(String roleIds, Integer userId) {
        userRoleMapper.deleteByUserId(userId);


        String[] stringids = roleIds.split(",");

        for (String stringid : stringids) {
            Integer roleId = Integer.parseInt(stringid);
            userRoleMapper.insertUserRole(userId,roleId);

        }


    }

    /**
     *
     * @param id    userId
     * @return
     */
    @Override
    public Permission getPermissionRootByUserId(Integer id) {

        Permission permissionRoot = null;

        Map<Integer, Permission> map = new HashMap<>();

        List<Permission> permissions = permissionMapper.selectPermissionOfUser(id);

        //级联
        for (Permission permission : permissions) {
            map.put(permission.getId(),permission);
        }

        for (Permission permission : permissions) {
            Permission child = permission;
            if(child.getPid() == null){
                permissionRoot = permission;
            }else{
                Permission parent = map.get(permission.getPid());
                parent.getChildren().add(child);
            }
        }
        return permissionRoot;


    }





    /*@Override
    public void doDeleteBatch(String ids) {
        String[] stringids = ids.split(",");
        for (String stringid : stringids) {
            Integer id = Integer.parseInt(stringid);
            userMapper.deleteByPrimaryKey(id);
        }
    }*/
}
