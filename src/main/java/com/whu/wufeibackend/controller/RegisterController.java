package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.DTO.SimpleRegisterRequest;
import com.whu.wufeibackend.DTO.SimpleRegisterResponse;
import com.whu.wufeibackend.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通用注册控制器
 * 处理团队需求文档中指定的 /api/register 路径
 * 
 * @author 无废技术组
 * @since 2024-06-23
 */
@RestController
@CrossOrigin(origins = "*")
public class RegisterController {
    
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * 通用注册接口
     * 符合团队需求文档：POST /api/register
     * 只需要用户名和密码，自动设置为普通用户注册
     * 返回简化的响应格式
     * 
     * @param request 注册请求（只包含用户名和密码）
     * @return 注册结果（简化版响应格式）
     */
    @PostMapping("/api/register")
    public SimpleRegisterResponse register(@Valid @RequestBody SimpleRegisterRequest request) {
        logger.info("【注册请求】收到注册请求 - 用户名: {}", request.getUsername());
        logger.debug("【注册请求】请求详情: {}", request.toString());
        
        try {
            // 执行注册逻辑
            logger.info("【注册处理】开始处理用户注册 - 用户名: {}", request.getUsername());
            SimpleRegisterResponse response = userService.register(request);
            
            logger.info("【注册成功】用户注册成功 - 用户名: {}, 用户类型: {}", 
                       request.getUsername(), response.getUserInfo().getUser_type());
            logger.debug("【注册响应】响应详情: {}", response.toString());
            
            return response;
            
        } catch (Exception e) {
            logger.error("【注册失败】用户注册失败 - 用户名: {}, 错误: {}", 
                        request.getUsername(), e.getMessage(), e);
            
            // 如果发生错误，返回错误消息格式
            SimpleRegisterResponse errorResponse = new SimpleRegisterResponse();
            errorResponse.setMessage("注册失败：" + e.getMessage());
            
            logger.warn("【注册响应】返回错误响应 - 消息: {}", errorResponse.getMessage());
            return errorResponse;
        }
    }
} 