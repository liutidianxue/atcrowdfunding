package com.atguigu.atcrowdfunding.manager.service.impl;

import com.atguigu.atcrowdfunding.bean.Advertisement;
import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.manager.mapper.AdvertisementMapper;
import com.atguigu.atcrowdfunding.manager.service.AdvertService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hewei
 * @date 2019/10/5 - 13:57
 */
@Service
public class AdvertServiceImpl implements AdvertService {

    @Autowired
    private AdvertisementMapper advertisementMapper;


    @Override
    public PageInfo queryPage(Map<String, Object> map) {
        String queryText = (String)map.get("queryText");
        if(!StringUtils.isBlank(queryText)){
            if (queryText.contains("%")){
                queryText = queryText.replaceAll("%","\\\\%");
            }
            queryText = "%"+queryText+"%";
            map.put("queryText",queryText);
        }
        PageHelper.startPage((Integer) map.get("page"),(Integer)map.get("pagesize"));
        List<Advertisement> advertisements = advertisementMapper.selectAllByName(map);    //非条件查询和根据名字查询合体了，实在不知道取什么名字，就这样吧。
        PageInfo pageInfo = new PageInfo(advertisements);
        return pageInfo;
    }

    @Override
    public Advertisement queryById(Integer id) {
        return advertisementMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateAdvert(Advertisement advertisement) {
        advertisementMapper.updateByPrimaryKey(advertisement);
    }

    @Override
    public void deleteAdvert(Integer id) {
        advertisementMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insertAdvert(Advertisement advert) {
        advertisementMapper.insert(advert);
    }


}
