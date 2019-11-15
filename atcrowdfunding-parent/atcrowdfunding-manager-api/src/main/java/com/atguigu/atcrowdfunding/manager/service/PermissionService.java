package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.ex.PermissionEx;

import java.util.List;
import java.util.Set;

/**
 * @author hewei
 * @date 2019/10/2 - 17:29
 */
public  interface PermissionService {
      List<Permission> getAll();

      Permission getRootpermission();

      List<PermissionEx> getChildren(Integer id);


      void add(Permission permission);

      void update(Permission permission);


      Permission queryBuId(Integer id);

      void delete(Integer id);

    /**
     * 重载的方法，查询所有的Permission并把角色拥有的权限check属性设置为true,然后转化为PermissionEx并返回
     * @param id roleId
     * @return
     */
      List<PermissionEx> getAllEx(Integer id);

      void saveRolePermissionRelationship(Integer roleId, List<Integer> ids);

    /**
     * 仅仅只查询所有的Permission然后转化为PermissionEx并返回
     * @return
     */
      List<PermissionEx> getAllEx();

    /**
     * 根据用户id查询改用户用户的访问权限路径们
     * @param userId
     * @return
     */
      Set<String> getUrlsOfUser(Integer userId);
}
