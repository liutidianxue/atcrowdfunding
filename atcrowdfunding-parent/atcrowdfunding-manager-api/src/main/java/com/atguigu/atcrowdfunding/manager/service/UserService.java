package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.bean.UserVo;
import com.atguigu.atcrowdfunding.exception.LoginFailException;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author hewei
 * @date 2019/9/28 - 2:26
 */
public interface UserService {
    User queryUserlogin(Map<String, String> paramMap) throws LoginFailException;


    PageInfo queryUserPage(Map<String,Object> map);

    int saveUser(User user);

    User queryById(Integer id);

    void updateUser(User user);

    void deleteUser(Integer id);

    void doDeleteBatch(UserVo userVo);

    List<Role> getAllRoles();

    List<Integer> getRolesIdsByUserId(Integer id);

    void updateUserRoles(String roleIds, Integer userId);

    Permission getPermissionRootByUserId(Integer id);


    //void doDeleteBatch(String ids);
}
