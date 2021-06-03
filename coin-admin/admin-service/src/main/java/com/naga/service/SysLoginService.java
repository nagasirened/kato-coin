package com.naga.service;

import com.naga.model.LoginResult;

public interface SysLoginService {

    /**
     * 登录
     */
    LoginResult login(String username, String password);
}
