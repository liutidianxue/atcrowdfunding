package com.atguigu.atcrowdfunding.manager.mapper;

import com.atguigu.atcrowdfunding.bean.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRole record);

    UserRole selectByPrimaryKey(Integer id);

    List<UserRole> selectAll();

    int updateByPrimaryKey(UserRole record);

    @Select("select roleid from t_user_role where userid=#{id}")
    List<Integer> getRolesIdsByUserId(Integer id);

    @Delete("delete from t_user_role where userid=#{userId}")
    void deleteByUserId(Integer userId);

    @Insert("insert into t_user_role values(null,#{userId},#{roleId})")
    void insertUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
}

