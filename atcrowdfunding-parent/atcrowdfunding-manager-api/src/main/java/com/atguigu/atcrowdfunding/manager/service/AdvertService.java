package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.Advertisement;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author hewei
 * @date 2019/10/5 - 13:57
 */
public interface AdvertService {
    PageInfo queryPage(Map<String, Object> map);

    Advertisement queryById(Integer id);


    void updateAdvert(Advertisement advertisement);

    void deleteAdvert(Integer id);

    void insertAdvert(Advertisement advert);
}
