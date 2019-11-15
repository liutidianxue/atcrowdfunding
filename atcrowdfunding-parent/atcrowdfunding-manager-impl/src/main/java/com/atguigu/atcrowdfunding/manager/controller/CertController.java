package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.manager.service.CertService;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hewei
 * @date 2019/10/1 - 16:07
 */
@Controller
@RequestMapping("/cert")
public class CertController {
    @Autowired
    private CertService certService;

    @RequestMapping("index")
    public String index(){
        return "cert/index";
    }

    @RequestMapping("/add")
    public String add(Cert cert){
        return "cert/add";
    }

    //异步请求分页查询和条件查询分页2者合1了。
    @ResponseBody
    @RequestMapping("/certlists")
    public AjaxResult certlists(@RequestParam(value="page",required = false,defaultValue = "1") Integer page,
                                @RequestParam(value="pagesize",required = false,defaultValue = "10")Integer pagesize,
                                String queryText){
        AjaxResult result = new AjaxResult();
        try {
            Map<String,Object> map = new HashMap<>();
            map.put("page",page);
            map.put("pagesize", pagesize);
            map.put("queryText", queryText);



            PageInfo pageInfo = certService.queryCertPage(map);
            result.setPageInfo(pageInfo);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("数据查询错误");
            e.printStackTrace();
        }
        return result;
    }




    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(Cert cert){
        AjaxResult result = new AjaxResult();

        if (StringUtils.isBlank(cert.getName())){
            return null;
        }
        try {
            int count = certService.saveCert(cert);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("保存角色失败");
            e.printStackTrace();
        }
        return result;
    }


    //到修改用户页面，并且回显数据
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(Integer id){
        ModelAndView mv = new ModelAndView();
        Cert cert = certService.queryById(id);

        mv.addObject("cert", cert);

        mv.setViewName("cert/update");
        return mv;
    }


    @ResponseBody
    @RequestMapping("/doUpdate")
    public AjaxResult doUpdate(Cert cert){
        AjaxResult result = new AjaxResult();
        try {
            certService.updateCert(cert);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/doDelete")
    public AjaxResult doDelete(Integer id){
        AjaxResult result = new AjaxResult();
        try {
            certService.deleteCert(id);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }




    //接收一个参数名带多个值
    @ResponseBody
    @RequestMapping("/doDeleteBatch")
    public AjaxResult doDeleteBatch(String ids){
        AjaxResult result = new AjaxResult();
        try {
            boolean success = certService.doDeleteBatch(ids);
            result.setSuccess(success);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }
}
