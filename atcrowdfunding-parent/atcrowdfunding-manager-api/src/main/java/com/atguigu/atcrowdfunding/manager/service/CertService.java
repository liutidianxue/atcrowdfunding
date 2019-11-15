package com.atguigu.atcrowdfunding.manager.service;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author hewei
 * @date 2019/10/1 - 16:30
 */
public interface CertService {
    PageInfo queryCertPage(Map<String, Object> map);

    int saveCert(Cert cert);

    Cert queryById(Integer id);

    void updateCert(Cert cert);

    void deleteCert(Integer id);

    boolean doDeleteBatch(String ids);
}
