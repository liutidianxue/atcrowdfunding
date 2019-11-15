package com.atguigu.atcrowdfunding.manager.service.impl;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.mapper.CertMapper;
import com.atguigu.atcrowdfunding.manager.service.CertService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hewei
 * @date 2019/10/1 - 16:31
 */
@Service
public class CertServiceImpl implements CertService {

    @Autowired
    private CertMapper certMapper;

    @Override
    public PageInfo queryCertPage(Map<String, Object> map) {
        String queryText = (String)map.get("queryText");
        if(!StringUtils.isBlank(queryText)){
            if (queryText.contains("%")){
                queryText = queryText.replaceAll("%","\\\\%");
            }
            queryText = "%"+queryText+"%";
            map.put("queryText",queryText);
        }
        PageHelper.startPage((Integer) map.get("page"),(Integer)map.get("pagesize"));
        List<Cert> certs = certMapper.selectAllByName(map);    //非条件查询和根据名字查询合体了，实在不知道取什么名字，就这样吧。
        PageInfo pageInfo = new PageInfo(certs);
        return pageInfo;
    }

    @Override
    public int saveCert(Cert cert) {
        return certMapper.insert(cert);
    }

    @Override
    public Cert queryById(Integer id) {
        return certMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateCert(Cert cert) {
        certMapper.updateByPrimaryKey(cert);
    }

    @Override
    public void deleteCert(Integer id) {
        certMapper.deleteByPrimaryKey(id);
    }

    @Override
    public boolean doDeleteBatch(String ids) {
        String[] stringids = ids.split(",");
        int count = 0;
        for (String stringid : stringids) {
            Integer id = Integer.parseInt(stringid);
            certMapper.deleteByPrimaryKey(id);
            count++;
        }

        return stringids.length == count;
    }
}
