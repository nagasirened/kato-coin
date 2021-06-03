package com.naga.config.mybatisplus;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;


/**
 * author: ZGF
 * context : 自动填充组件
 *
 * 使用
 *
     @TableField(fill = FieldFill.INSERT)
     private String createdTime;
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = getCurrentUserId();
        Date now = new Date();
        this.strictInsertFill(metaObject, "createBy", Long.class, userId);
        this.strictInsertFill(metaObject, "created", Date.class, now);
        this.strictInsertFill(metaObject, "lastUpdateTime", Date.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = getCurrentUserId();
        this.strictUpdateFill(metaObject, "modifyBy", Long.class, userId); // 修改人
        this.strictUpdateFill(metaObject, "lastUpdateTime", Date.class, new Date());
    }

    /**
     * 获取当前操作的用户对象
     * @return
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 从安全的上下文里面获取用户的ud
        if(Objects.nonNull(authentication)){
            String s = authentication.getPrincipal().toString();
            if(StringUtils.equals("anonymousUser", s)){      //是因为用户没有登录访问时,就是这个用户
                return null;
            }
            return Long.valueOf(s);
        }
        return null;
    }
}
