package com.atguigu.atcrowdfunding.manager.service.impl;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.ex.PermissionEx;
import com.atguigu.atcrowdfunding.manager.mapper.PermissionMapper;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hewei
 * @date 2019/10/2 - 17:30
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getAll() {
        return permissionMapper.selectAll();
    }

    @Override
    public Permission getRootpermission() {
        return permissionMapper.getRootpermission();
    }

    /**
     * 根据角色id得到该id下面的角色们，改我的扩展类的集合对象了
     * @param id
     * @return
     */
    @Override
    public List<PermissionEx> getChildren(Integer id) {

        List<Permission> root2 = permissionMapper.getChildren(id);

        List<PermissionEx> root2Exs = root2.stream().map(permission -> {
            //看需求是否setOpen(true)
            PermissionEx root2Ex = new PermissionEx();
            BeanUtils.copyProperties(permission,root2Ex);
            //root2Ex.setOpen(true);
            return root2Ex;
        }).collect(Collectors.toList());

        return root2Exs;
    }

    @Override
    public void add(Permission permission) {
        permissionMapper.insert(permission);
    }

    @Override
    public void update(Permission permission) {
        permissionMapper.updateByPrimaryKey(permission);
    }

    @Override
    public Permission queryBuId(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Integer id) {
        permissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<PermissionEx> getAllEx(Integer id) {
        //定义将来要返回给浏览器的值,视频说Ztree插件要求就是传list集合到前台，不知道数组或者set集合行不行
        List<PermissionEx> result = new ArrayList<>();

        //定义这个map就是为了减少循环
        Map<Integer,PermissionEx> map = new HashMap<>();

        //不是要回显的话，只查一次数据库就好了
        List<Permission> permissions = permissionMapper.selectAll();

        //List<Permission>转化为List<PermissionEx>，并且回显打√
        List<PermissionEx> permissionExes = permissions.stream().map(permission -> {
            PermissionEx permissionEx = new PermissionEx();
            BeanUtils.copyProperties(permission,permissionEx);
            permissionEx.setOpen(true);
            List<Integer> pids = permissionMapper.getPidsByRoleId(id);
            if (pids.contains(permission.getId())){
                permissionEx.setChecked(true);
            }
            return permissionEx;
        }).collect(Collectors.toList());

        //级联
        for (PermissionEx permissionEx : permissionExes) {
            map.put(permissionEx.getId(),permissionEx);
        }

        for (PermissionEx permissionEx : permissionExes) {
            PermissionEx child = permissionEx;
            if(permissionEx.getPid() == null){
                PermissionEx root = permissionEx;
                result.add(root);
            }else{
                PermissionEx parent = map.get(permissionEx.getPid());
                parent.getChildren().add(child);
            }
        }
        return result;
    }

    @Override
    public void saveRolePermissionRelationship(Integer roleId, List<Integer> ids) {
        this.permissionMapper.deleteByRoleId(roleId);

        int totalcounts = 0;
        for (Integer permissionId : ids) {
            this.permissionMapper.saveRolePermissionRelationship(roleId,permissionId);
            totalcounts++;
        }
        if(totalcounts != ids.size()){
            throw new RuntimeException("保存错误");
        }
    }

    @Override
    public List<PermissionEx> getAllEx() {
        //定义将来要返回给浏览器的值,视频说Ztree插件要求就是传list集合到前台，不知道数组或者set集合行不行
        List<PermissionEx> result = new ArrayList<>();

        Map<Integer,PermissionEx> map = new HashMap<>();

        List<Permission> permissions = permissionMapper.selectAll();

        List<PermissionEx> permissionExes = permissions.stream().map(permission -> {
            PermissionEx permissionEx = new PermissionEx();
            BeanUtils.copyProperties(permission,permissionEx);
            permissionEx.setOpen(true);
            return permissionEx;
        }).collect(Collectors.toList());

        //级联
        for (PermissionEx permissionEx : permissionExes) {
            map.put(permissionEx.getId(),permissionEx);
        }

        for (PermissionEx permissionEx : permissionExes) {
            PermissionEx child = permissionEx;
            if(permissionEx.getPid() == null){
                PermissionEx root = permissionEx;
                result.add(root);
            }else{
                PermissionEx parent = map.get(permissionEx.getPid());
                parent.getChildren().add(child);
            }
        }
        return result;
    }

    @Override
    public Set<String> getUrlsOfUser(Integer userId) {
        List<Permission> permissions =  permissionMapper.selectPermissionOfUser(userId);
        Set<String> urlsOfUser = permissions.stream().map(permission -> {
           String urlOfUser = "/" + permission.getUrl();
           return urlOfUser;
        }).collect(Collectors.toSet());
        return urlsOfUser;

    }


}
