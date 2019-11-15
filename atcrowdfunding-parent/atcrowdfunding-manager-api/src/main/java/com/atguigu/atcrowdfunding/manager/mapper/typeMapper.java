package com.atguigu.atcrowdfunding.manager.mapper;

import com.atguigu.atcrowdfunding.bean.Type;
import java.util.List;

public interface typeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Type record);

    Type selectByPrimaryKey(Integer id);

    List<Type> selectAll();

    int updateByPrimaryKey(Type record);
}