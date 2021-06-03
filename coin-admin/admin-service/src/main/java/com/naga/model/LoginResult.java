package com.naga.model;

import com.naga.domain.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * 登录成功的结果
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "登录的结果")
public class LoginResult{

    /**
     * 登录成功的token，来自授权服务器
     */
    @ApiModelProperty(value = "登录成功的token，来自授权服务器")
    private String token;

    /**
     * 该用户的菜单
     */
    @ApiModelProperty(value = "菜单集合")
    private List<SysMenu> menus;

    /**
     * 用户权限信息
     */
    @ApiModelProperty(value = "权限集合")
    private List<SimpleGrantedAuthority> authorities;

}
