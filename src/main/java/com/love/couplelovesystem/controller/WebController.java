package com.love.couplelovesystem.controller;

import com.love.couplelovesystem.module.system.service.SystemConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * 前端页面路由
 */
@Controller
public class WebController {

    @Resource
    private SystemConfigService configService;

    /**
     * 首页 - 密码验证页
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("slogan", configService.getConfigValue("page_slogan"));
        return "password";
    }

    /**
     * 主页面
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    /**
     * 情绪急救专区
     */
    @GetMapping("/comfort")
    public String comfort() {
        return "comfort";
    }

    /**
     * 互动升温专区
     */
    @GetMapping("/interaction")
    public String interaction() {
        return "interaction";
    }

    /**
     * 恋爱记录专区
     */
    @GetMapping("/memory")
    public String memory() {
        return "memory";
    }

    /**
     * 偏爱特权专区
     */
    @GetMapping("/privilege")
    public String privilege() {
        return "privilege";
    }

    /**
     * 后台登录页
     */
    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin/dashboard";
    }

    /**
     * 后台仪表盘
     */
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    /**
     * 后台首页重定向
     */
    @GetMapping("/admin")
    public String admin() {
        return "admin/dashboard";
    }
}
