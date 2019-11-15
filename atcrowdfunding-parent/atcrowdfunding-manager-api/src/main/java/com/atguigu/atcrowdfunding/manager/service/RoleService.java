package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.Role;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author hewei
 * @date 2019/10/1 - 16:30
 */
public interface RoleService {
    PageInfo queryRolePage(Map<String, Object> map);

    int saveRole(Role role);

    Role queryById(Integer id);

    void updateRole(Role role);

    void deleteRole(Integer id);

    boolean doDeleteBatch(String ids);
}
