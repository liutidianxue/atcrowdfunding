package com.atguigu.atcrowdfunding.manager.mapper;

import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    Role selectByPrimaryKey(Integer id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    @Select({"<script>",
            "SELECT * FROM t_role",
            "WHERE 1=1",
            "<when test='queryText!=null'>",
            "and name like #{queryText}",
            "</when>",
            "</script>"})
    List<Role> selectAllByName(Map<String, Object> map);
}