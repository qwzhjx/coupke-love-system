package com.love.couplelovesystem.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc 配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 配置上传文件静态资源访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }

    /**
     * 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 后台管理接口拦截
        registry.addInterceptor(new com.love.couplelovesystem.module.admin.interceptor.AdminAuthInterceptor())
                .addPathPatterns("/api/admin/**")
                .excludePathPatterns("/api/admin/login");
    }
}
