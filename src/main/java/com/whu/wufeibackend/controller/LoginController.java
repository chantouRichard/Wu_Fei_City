package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.dto.ApiResponse;
import com.whu.wufeibackend.dto.LoginResponse;
import com.whu.wufeibackend.dto.SimpleLoginRequest;
import com.whu.wufeibackend.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通用登录控制器
 * 处理团队需求文档中指定的 /api/login 路径
 * 
 * @author 无废技术组
 * @since 2024-06-23
 */
@RestController
@CrossOrigin(origins = "*")
public class LoginController {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * 通用登录接口
     * 符合团队需求文档：POST /api/login
     * 只需要用户名和密码，自动设置为普通用户登录
     * 
     * @param request 登录请求（只包含用户名和密码）
     * @return 登录结果（标准ApiResponse格式）
     */
    @PostMapping("/api/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody SimpleLoginRequest request) {
        logger.info("【登录请求】收到登录请求 - 用户名: {}", request.getUsername());
        logger.debug("【登录请求】请求详情: {}", request.toString());
        
        try {
            // 执行登录逻辑
            LoginResponse loginResponse = userService.login(request);
            
            if (loginResponse.getSuccess()) {
                
                return ApiResponse.success("登录成功", loginResponse);
            } else {
                
                return ApiResponse.error(400, loginResponse.getReason());
            }
            
        } catch (Exception e) {
            logger.error("【登录异常】用户登录过程中发生异常 - 用户名: {}, 错误: {}", 
                        request.getUsername(), e.getMessage(), e);
            
            // 返回系统错误响应
            ApiResponse<LoginResponse> errorResponse = ApiResponse.error("系统异常：" + e.getMessage());
            logger.warn("【登录响应】返回异常响应 - 消息: {}", errorResponse.getMessage());
            
            return errorResponse;
        }
    }
} 