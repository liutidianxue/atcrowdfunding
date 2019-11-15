package com.atguigu.atcrowdfunding.util;

import com.github.pagehelper.PageInfo;

/**
 * @author hewei
 * @date 2019/9/28 - 16:12
 */
public class AjaxResult {
    private boolean success;
    private String message;
    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
