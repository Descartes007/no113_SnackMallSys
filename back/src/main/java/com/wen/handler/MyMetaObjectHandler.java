package com.wen.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        // 如果需要设置creator字段，可以从当前登录用户中获取
        // this.strictInsertFill(metaObject, "creator", String.class, getCurrentUserId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        // 如果需要设置updater字段，可以从当前登录用户中获取
        // this.strictUpdateFill(metaObject, "updater", String.class, getCurrentUserId());
    }
}