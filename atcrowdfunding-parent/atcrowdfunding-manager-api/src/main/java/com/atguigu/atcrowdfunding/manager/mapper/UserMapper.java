package com.atguigu.atcrowdfunding.manager.mapper;

import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.bean.UserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    @Select("select * from t_user where loginacct=#{loginacct} and userpswd=#{userpswd}")
    User queryUserlogin(Map<String, String> paramMap);

    @Select({"<script>",
            "SELECT * FROM t_user",
            "WHERE 1=1",
            "<when test='queryText!=null'>",
            "and loginacct like #{queryText}",
            "</when>",
            "order by createtime desc",
            "</script>"})
    List<User> selectAllByLoginacct(Map<String,Object> map);


    void DeleteBatchByVo(UserVo userVo);
}