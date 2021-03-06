package com.atguigu.atcrowdfunding.manager.service.impl;

import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.mapper.PermissionMapper;
import com.atguigu.atcrowdfunding.manager.mapper.RoleMapper;
import com.atguigu.atcrowdfunding.manager.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hewei
 * @date 2019/10/1 - 16:31
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public PageInfo queryRolePage(Map<String, Object> map) {
        String queryText = (String)map.get("queryText");
        if(!StringUtils.isBlank(queryText)){
            if (queryText.contains("%")){
                queryText = queryText.replaceAll("%","\\\\%");
            }
            queryText = "%"+queryText+"%";
            map.put("queryText",queryText);
        }
        PageHelper.startPage((Integer) map.get("page"),(Integer)map.get("pagesize"));
        List<Role> roles = roleMapper.selectAllByName(map);    //非条件查询和根据名字查询合体了，实在不知道取什么名字，就这样吧。
        PageInfo pageInfo = new PageInfo(roles);
        return pageInfo;
    }

    @Override
    public int saveRole(Role role) {
        return roleMapper.insert(role);
    }

    @Override
    public Role queryById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateRole(Role role) {


        roleMapper.updateByPrimaryKey(role);
    }

    /**
     *
     * @param id roleId
     */
    @Override
    public void deleteRole(Integer id) {
        this.permissionMapper.deleteByRoleId(id);

        roleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public boolean doDeleteBatch(String ids) {
        String[] stringids = ids.split(",");
        int count = 0;
        for (String stringid : stringids) {
            Integer id = Integer.parseInt(stringid);
            roleMapper.deleteByPrimaryKey(id);
            count++;
        }

        return stringids.length == count;
    }
}
