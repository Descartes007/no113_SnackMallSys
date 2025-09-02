package com.wen.service.impl;

import com.wen.dao.UserRoleDao;
import com.wen.entity.UserRole;
import com.wen.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 
 * @email wendb.top@aliyun.com
 * @date 2020/10/31 15:49
 * @description 用户角色业务逻辑
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public List<UserRole> selectByUserId(Integer userId) {
        return userRoleDao.selectByUserId(userId);
    }

    @Override
    public List<UserRole> selectAll() {
        return userRoleDao.selectAll();
    }

    @Override
    public Boolean insertData(UserRole userRole) {
        return userRoleDao.insertData(userRole);
    }

    @Override
    public Boolean deleteById(Integer userId) {
        return userRoleDao.deleteById(userId);
    }

    @Override
    public List<Map<String, Object>> selectRoleByUserId(Integer userId) {
        return userRoleDao.selectRoleByUserId(userId);
    }

    @Override
    public Boolean existsRole(Integer roleId) {
        return userRoleDao.existsRole(roleId);
    }
}
