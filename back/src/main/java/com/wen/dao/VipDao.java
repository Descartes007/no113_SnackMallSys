package com.wen.dao;

import com.wen.entity.Vip;

/**
 * 
 * @email wendb.top@aliyun.com
 * @date 2020/11/20 16:34
 * @description vip表
 */
public interface VipDao extends BaseDao<Vip> {
    /**
     * 查询会员是否存在
     * @param accountNumber 用户账号
     * @return 会员信息是否存在
     */
    Boolean existsVip(String accountNumber);
}
