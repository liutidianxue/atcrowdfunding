package com.atguigu.atcrowdfunding.manager.mapper;

import com.atguigu.atcrowdfunding.bean.Advertisement;
import com.atguigu.atcrowdfunding.bean.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface AdvertisementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Advertisement record);

    Advertisement selectByPrimaryKey(Integer id);

    List<Advertisement> selectAll();


    int updateByPrimaryKey(Advertisement record);

    @Select({"<script>",
            "SELECT * FROM t_advertisement",
            "WHERE 1=1",
            "<when test='queryText!=null'>",
            "and name like #{queryText}",
            "</when>",
            "</script>"})
    List<Advertisement> selectAllByName(Map<String, Object> map);
}