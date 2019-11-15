package com.atguigu.atcrowdfunding.activitilistener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @author hewei
 * @date 2019/10/6 - 12:38
 */
public class NoListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        System.out.println("审批拒绝");
    }
}
