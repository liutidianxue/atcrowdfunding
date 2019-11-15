package com.atguigu.atcrowdfunding.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hewei
 * @date 2019/10/1 - 4:15
 */
public class UserVo {
    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
