package com.love.couplelovesystem.module.system.service;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.love.couplelovesystem.entity.SystemConfig;
import com.love.couplelovesystem.mybatis.mapper.SystemConfigMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置服务
 */
@Service
public class SystemConfigService {

    @Resource
    private SystemConfigMapper configMapper;

    /**
     * 初始化默认配置
     */
    @PostConstruct
    public void initConfig() {
        initIfAbsent("access_password", "5201314");
        initIfAbsent("page_slogan", "🎀 Hello Kitty · 专属宠爱只属于你 🎀");
        initIfAbsent("boy_nickname", "宝宝");
        initIfAbsent("girl_nickname", "女朋友");
        initIfAbsent("love_start_date", "2024-01-01");
        initIfAbsent("theme_color", "#FFB6C1");
    }

    private void initIfAbsent(String key, String value) {
        QueryWrapper<SystemConfig> qw = new QueryWrapper<>();
        qw.eq("config_key", key);
        if (configMapper.selectOne(qw) == null) {
            SystemConfig config = new SystemConfig();
            config.setConfigKey(key);
            config.setConfigValue(value);
            configMapper.insert(config);
        }
    }

    /**
     * 获取所有配置
     */
    public Map<String, String> getAllConfig() {
        List<SystemConfig> list = configMapper.selectList(null);
        Map<String, String> map = new HashMap<>();
        for (SystemConfig config : list) {
            map.put(config.getConfigKey(), config.getConfigValue());
        }
        return map;
    }

    /**
     * 获取单个配置值
     */
    public String getConfigValue(String key) {
        QueryWrapper<SystemConfig> qw = new QueryWrapper<>();
        qw.eq("config_key", key);
        SystemConfig config = configMapper.selectOne(qw);
        return config != null ? config.getConfigValue() : null;
    }

    /**
     * 更新配置
     */
    public void updateConfig(Map<String, String> configs) {
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            QueryWrapper<SystemConfig> qw = new QueryWrapper<>();
            qw.eq("config_key", entry.getKey());
            SystemConfig config = configMapper.selectOne(qw);
            if (config != null) {
                config.setConfigValue(entry.getValue());
                configMapper.updateById(config);
            }
        }
    }

    /**
     * 验证访问密码
     */
    public boolean verifyAccessPassword(String password) {
        String storedPassword = getConfigValue("access_password");
        if (storedPassword == null) {
            return "5201314".equals(password);
        }
        return storedPassword.equals(password);
    }

    /**
     * 获取公开配置（供前端使用）
     */
    public Map<String, String> getPublicConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("slogan", getConfigValue("page_slogan"));
        config.put("boyNickname", getConfigValue("boy_nickname"));
        config.put("girlNickname", getConfigValue("girl_nickname"));
        config.put("loveStartDate", getConfigValue("love_start_date"));
        config.put("themeColor", getConfigValue("theme_color"));
        return config;
    }
}
