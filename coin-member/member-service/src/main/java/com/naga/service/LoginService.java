package com.naga.service;

import com.naga.vo.LoginForm;
import com.naga.vo.LoginUserVO;

public interface LoginService {

    /**
     * 登录请求
     * @param loginForm 登录请求参数
     * @return LoginUserVO
     */
    LoginUserVO login(LoginForm loginForm);
}
