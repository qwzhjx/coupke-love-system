package com.love.couplelovesystem.module.admin.controller;

import com.love.couplelovesystem.common.utils.R;
import com.love.couplelovesystem.module.admin.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 后台管理-认证控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public R login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        if (username == null || password == null) {
            return R.error("用户名和密码不能为空");
        }
        try {
            String token = adminService.login(username, password);
            return R.ok("登录成功", token);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public R changePassword(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        try {
            adminService.changePassword(username, oldPassword, newPassword);
            return R.ok("密码修改成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
