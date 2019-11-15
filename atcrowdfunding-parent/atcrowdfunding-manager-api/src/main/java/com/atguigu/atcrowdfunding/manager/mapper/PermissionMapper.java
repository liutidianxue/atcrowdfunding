package com.atguigu.atcrowdfunding.manager.mapper;

import com.atguigu.atcrowdfunding.bean.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    Permission selectByPrimaryKey(Integer id);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);

    @Select("select * from t_permission where pid is null")
    Permission getRootpermission();

    @Select("select * from t_permission where pid=#{id}")
    List<Permission> getChildren(Integer id);


    @Select("select permissionid from t_role_permission where roleid=#{id}")
    List<Integer> getPidsByRoleId(Integer id);

    @Delete("delete from t_role_permission where roleid=#{roleId}")
    void deleteByRoleId(Integer roleId);



    @Insert("insert into t_role_permission values(null,#{roleId},#{permissionId})")
    void saveRolePermissionRelationship(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);

    /**
     * 根据用户拿到所有
     * @param userId
     * @return
     */
    @Select("select distinct t_permission.* " +
            "from t_permission,t_role_permission,t_role,t_user_role,t_user " +
            "where t_permission.id = t_role_permission.`permissionid` " +
            "and t_role_permission.`roleid` = t_role.id " +
            "and t_role.`id`=t_user_role.`roleid` " +
            "and t_user_role.`userid` = t_user.`id` " +
            "and t_user.id=#{userId} order by t_permission.id")
    List<Permission> selectPermissionOfUser(Integer userId);
}