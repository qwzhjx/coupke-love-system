package com.love.couplelovesystem.module.admin.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.love.couplelovesystem.common.utils.JwtUtils;
import com.love.couplelovesystem.common.utils.R;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台管理认证拦截器
 */
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS 预检请求放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || !JwtUtils.validateToken(token)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(R.unauthorized()));
            return false;
        }
        return true;
    }
}
