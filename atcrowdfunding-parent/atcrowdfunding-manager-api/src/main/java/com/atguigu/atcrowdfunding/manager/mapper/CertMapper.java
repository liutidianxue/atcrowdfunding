package com.atguigu.atcrowdfunding.manager.mapper;

import com.atguigu.atcrowdfunding.bean.Cert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface CertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cert record);

    Cert selectByPrimaryKey(Integer id);

    List<Cert> selectAll();

    int updateByPrimaryKey(Cert record);

    @Select({"<script>",
            "SELECT * FROM t_cert",
            "WHERE 1=1",
            "<when test='queryText!=null'>",
            "and name like #{queryText}",
            "</when>",
            "</script>"})
    List<Cert> selectAllByName(Map<String, Object> map);
}