package com.sysu.service;

import com.sysu.model.User;

/**
 * 用户服务
 */
public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);

    /**
     * 新方法获取数字，用于测试mock是否生效
     * @return
     */
    default short getNumber(){
        return 1;
    }
}
