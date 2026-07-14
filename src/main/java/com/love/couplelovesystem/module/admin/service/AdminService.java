package com.love.couplelovesystem.module.admin.service;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.love.couplelovesystem.common.utils.JwtUtils;
import com.love.couplelovesystem.entity.Admin;
import com.love.couplelovesystem.mybatis.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import javax.annotation.Resource;

/**
 * 管理员服务
 */
@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    /**
     * 初始化默认管理员账号
     */
    @Order(Ordered.LOWEST_PRECEDENCE)
    @EventListener(ApplicationStartedEvent.class)
    public void initAdmin() {
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("username", "admin");
        if (adminMapper.selectOne(qw) == null) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            String salt = "fixed123";
            admin.setSalt(salt);
            admin.setPassword(SecureUtil.md5("admin123" + salt));
            adminMapper.insert(admin);
        }
    }

    /**
     * 登录验证
     */
    public String login(String username, String password) {
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("username", username);
        Admin admin = adminMapper.selectOne(qw);
        if (admin == null) {
            throw new RuntimeException("账号不存在");
        }
        String encrypted = SecureUtil.md5(password + admin.getSalt());
        if (!encrypted.equals(admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return JwtUtils.generateToken(admin.getId(), admin.getUsername());
    }

    /**
     * 修改密码
     */
    public void changePassword(String username, String oldPassword, String newPassword) {
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("username", username);
        Admin admin = adminMapper.selectOne(qw);
        if (admin == null) {
            throw new RuntimeException("账号不存在");
        }
        if (!SecureUtil.md5(oldPassword + admin.getSalt()).equals(admin.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }
        String newSalt = SecureUtil.md5(String.valueOf(System.currentTimeMillis()));
        admin.setSalt(newSalt);
        admin.setPassword(SecureUtil.md5(newPassword + newSalt));
        adminMapper.updateById(admin);
    }
}
