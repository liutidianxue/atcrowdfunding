package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.util.AjaxResult;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hewei
 * @date 2019/10/6 - 16:31
 */

@Controller
@RequestMapping("/process")
public class ProcessController {


    @Autowired
    RepositoryService repositoryService;

    @RequestMapping("/index")
    public String index(){
        return "process/index";
    }



    @RequestMapping("/processlists")
//    public ResponseEntity<List<Map<String,Object>>> processlists(
    public ResponseEntity<String> processlists(
            @RequestParam(value="page",required = false,defaultValue = "1") Integer page,
            @RequestParam(value="pagesize",required = false,defaultValue = "10")Integer pagesize){
        try {
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            //查询流程定义集合数据,可能出现了自关联,导致Jackson组件无法将集合序列化为JSON串.
            List<ProcessDefinition> listPage = processDefinitionQuery.listPage(page, pagesize);

            List<Map<String,Object>> mylistPage = new ArrayList<>();

            for (ProcessDefinition processDefinition : listPage) {

                Map<String,Object> pd = new HashMap<>();
                pd.put("id", processDefinition.getId());
                pd.put("name", processDefinition.getName());
                pd.put("key", processDefinition.getKey());
                pd.put("version", processDefinition.getVersion());

                mylistPage.add(pd);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return ResponseEntity.ok("ok");
    }



}
