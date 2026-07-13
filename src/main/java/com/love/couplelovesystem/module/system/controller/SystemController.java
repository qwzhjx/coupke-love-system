package com.love.couplelovesystem.module.system.controller;

import com.love.couplelovesystem.common.utils.R;
import com.love.couplelovesystem.module.system.service.SystemConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 系统-前端控制器
 */
@RestController
@RequestMapping("/api/system")
public class SystemController {

    @Resource
    private SystemConfigService configService;

    /**
     * 验证访问密码
     */
    @PostMapping("/verify-password")
    public R verifyPassword(@RequestBody Map<String, String> params) {
        String password = params.get("password");
        if (password == null || password.isEmpty()) {
            return R.error("请输入访问密码");
        }
        boolean valid = configService.verifyAccessPassword(password);
        if (valid) {
            return R.ok("验证通过 💕");
        }
        return R.error("密码错误，再想想~");
    }

    /**
     * 获取公开配置
     */
    @GetMapping("/config/public")
    public R publicConfig() {
        return R.ok(configService.getPublicConfig());
    }
}
