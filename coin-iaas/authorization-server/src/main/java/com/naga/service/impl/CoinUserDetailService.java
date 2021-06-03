package com.naga.service.impl;

import com.naga.constant.LoginConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service(value = "coinUserDetailService")
public class CoinUserDetailService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        // loginType 区分是管理员登录还是普通会员登录
        String loginType = servletRequestAttributes.getRequest().getParameter("login_type");
        if (StringUtils.isEmpty(loginType)) {
            throw new AuthenticationServiceException("登录类型不能为空");
        }
        UserDetails userDetails = null;
        try {
            /** refreshToken进行纠正，因为refreshToken时，不传username而是userId，username就没有了，因此查一下 */
            String grantType = servletRequestAttributes.getRequest().getParameter("grant_type");
            if (LoginConstant.REFRESH_TYPE.equals(grantType)) {
                username = adjustUsername(username, loginType);
            }

            switch (loginType) {
                case LoginConstant.ADMIN_TYPE:
                    userDetails = loadSysUserByUsername(username);
                    break;
                case LoginConstant.MEMBER_TYPE:
                    userDetails = loadMemberUserByUsername(username);
                    break;
                default:
                    throw new AuthenticationServiceException("暂不支持的登录方式");
            }

            /** jdbcTemplate.queryForObject 没有结果会直接抛出异常，捕捉一下 */
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return userDetails;
    }

    /**
     * 管理员用户
     * @param username
     * @return
     */
    private UserDetails loadSysUserByUsername(String username) {
        // 查询用户和权限信息，封装返回结果
        return jdbcTemplate.queryForObject(LoginConstant.QUERY_SYS_USER_SQL, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                if (resultSet.wasNull()) {
                    throw new UsernameNotFoundException("用户名不存在");
                }
                long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                int status = resultSet.getInt("status");
                return new User(String.valueOf(id), password,
                        status == 1, true, true, true,
                        getSysPermissions(id)
                );
            }

        }, /** 第三个参数替换SQL中的问好 */username);
    }

    /**
     * 普通会员用户
     * @param username
     * @return
     */
    private UserDetails loadMemberUserByUsername(String username) {
        return jdbcTemplate.queryForObject(LoginConstant.QUERY_MEMBER_SQL, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                if (resultSet.wasNull()) {
                    throw new UsernameNotFoundException("用户名不存在");
                }
                long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                int status = resultSet.getInt("status");
                return new User(String.valueOf(id), password,
                        status == 1, true, true, true,
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
                );
            }
        }, username, username /** SQL 里面用username 替代mobile和email */);
    }


    /**
     * 获取用户的权限
     * 超级管理员能获取全部权限，其他的管理员可以获取自己对应的权限
     */
    private Collection<? extends GrantedAuthority> getSysPermissions(long id) {
        String roleCode = jdbcTemplate.queryForObject(LoginConstant.QUERY_ROLE_CODE_SQL, String.class, id);
        List<String> permissions = null;
        if (StringUtils.equals(roleCode, LoginConstant.ROLE_ADMIN)) {
            permissions = jdbcTemplate.queryForList(LoginConstant.QUERY_ALL_PERMISSION, String.class);
        } else {
            permissions = jdbcTemplate.queryForList(LoginConstant.QUERY_PERMISSION_SQL, String.class, id);
        }

        if (CollectionUtils.isEmpty(permissions)) {
            return Collections.emptySet();
        }
        return permissions.stream().map(item -> new SimpleGrantedAuthority(item)).collect(Collectors.toList());
    }

    /**
     * 纠正用户的名称
     *
     * @param username  用户的id
     * @param loginType admin_type  member_type
     * @return
     */
    private String adjustUsername(String username, String loginType) {
        if (LoginConstant.ADMIN_TYPE.equals(loginType)) {
            // 管理员的纠正方式
            return jdbcTemplate.queryForObject(LoginConstant.QUERY_ADMIN_USER_WITH_ID,String.class ,username);
        }
        if (LoginConstant.MEMBER_TYPE.equals(loginType)) {
            // 会员的纠正方式
            return jdbcTemplate.queryForObject(LoginConstant.QUERY_MEMBER_USER_WITH_ID,String.class ,username);
        }
        return username;
    }

}
