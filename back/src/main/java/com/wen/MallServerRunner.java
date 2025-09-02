package com.wen;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.wen.constant.UserStatusEnum;
import com.wen.entity.SuperAdmin;
import com.wen.entity.User;
import com.wen.entity.UserRole;
import com.wen.service.UserRoleService;
import com.wen.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 初始化管理员
 */
@Component
public class MallServerRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MallServerRunner.class);

    private static final String DEFAULT_USERNAME = "超级管理员";

    private static final String DEFAULT_PASSWORD = "123456";

    SuperAdmin superAdmin;

    @Autowired
    UserService userService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setSuperAdmin(SuperAdmin superAdmin) {
        this.superAdmin = superAdmin;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Executing command line runner...");
        //需要指定email(必须指定)且系统中不存在该邮箱对应的用户，则进入初始化逻辑。
        if (StringUtils.isNotBlank(superAdmin.getEmail()) &&
                !userService.existsWithPrimaryKey(superAdmin.getEmail())) {
            User user = new User();
            user.setAccountNumber(superAdmin.getEmail());//email: admin@qq.com
            if (StringUtils.isBlank(superAdmin.getUserName())) {
                superAdmin.setUserName(DEFAULT_USERNAME);
            }
            user.setUserName(superAdmin.getUserName());
            if (StringUtils.isBlank(superAdmin.getPassword())) {
                superAdmin.setPassword(DEFAULT_PASSWORD);
            }
            String encodePassword = SaSecureUtil.md5BySalt(superAdmin.getPassword(), superAdmin.getEmail());
            user.setPassword(encodePassword);
            user.setUserState(true);
            user.setStatus(UserStatusEnum.ADMIN);
            //初始化管理员(插入唯一超级管理员)
            userService.insertData(user);
            //删除超级管理员相关的用户角色表
            userRoleService.deleteById(user.getUserId());
            //插入超级管理员相关的用户角色表
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(1);
            userRoleService.insertData(userRole);
        }
        log.info("系统初始化完成，服务已正常启动");
    }
}
