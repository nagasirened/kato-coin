package com.naga.constant;

public class LoginConstant {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * 管理人员登录
     */
    public static final String ADMIN_TYPE = "admin_type";

    /**
     * 会员登录
     */
    public static final String MEMBER_TYPE = "member_type";

    public static final String REFRESH_TYPE = "refresh_token";

    /** ==========================================管理员用户start========================================== */
    /**
     * 查询管理员用户的SQL
     */
    public static final String QUERY_SYS_USER_SQL = "select id, username, password, `status` from sys_user where username = ?";

    /**
     * 查询用户的权限，如果查询出来的code是ROLE_ADMIN的话，那么他就是管理员角色
     */
    public static final String QUERY_ROLE_CODE_SQL =
            "select r.code from sys_role r left join sys_user_role ur on r.id = ur.role_id where ur.user_id = ?";

    /**
     * 查询所有权限的名称，管理员拥有所有的权限
     */
    public static final String QUERY_ALL_PERMISSION = "select name from sys_privilege";

    /** ==========================================管理员用户end========================================== */




    /** ==========================================普通会员用户start ========================================== */
    public static final String QUERY_MEMBER_SQL = "select id, password, status from user where mobile = ? or email = ?";


    public static final String QUERY_PERMISSION_SQL =
            "select p.name from sys_privilege p left join sys_role_privilege rp on rp.privilege_id = p.id " +
                    "left join sys_user_role ur on rp.role_id = ur.role_id where ur.user_id = ?";

    /** ==========================================普通会员用户 end ========================================== */


    /**
     * 使用后台用户的id 查询用户名称
     */
    public static  final  String QUERY_ADMIN_USER_WITH_ID = "SELECT `username` FROM sys_user where id = ?" ;

    /**
     * 使用用户的id 查询用户名称
     */
    public static  final  String QUERY_MEMBER_USER_WITH_ID = "SELECT `mobile` FROM user where id = ?" ;
}
