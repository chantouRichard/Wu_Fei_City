package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.DTO.ApiResponse;
import com.whu.wufeibackend.DTO.LoginResponse;
import com.whu.wufeibackend.DTO.SimpleLoginRequest;
import com.whu.wufeibackend.DTO.SimpleRegisterRequest;
import com.whu.wufeibackend.DTO.SimpleRegisterResponse;
import com.whu.wufeibackend.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 居委会控制器
 * 处理居委会相关的功能：注册、登录、居委会专属业务等
 */
@RestController
@RequestMapping("/api/committee")
@CrossOrigin(origins = "*")
public class CommitteeController {
    
    private static final Logger logger = LoggerFactory.getLogger(CommitteeController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * 居委会注册
     * 
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public ApiResponse<SimpleRegisterResponse> register(@Valid @RequestBody SimpleRegisterRequest request) {
        logger.info("【居委会注册】收到注册请求 - 用户名: {}, 用户类型: {}", 
                   request.getUsername(), request.getUserType());
        logger.debug("【居委会注册】请求详情: {}", request.toString());
        
        try {
            // 确保只能注册居委会
            if (!"committee".equals(request.getUserType())) {
                logger.warn("【居委会注册】非居委会用户尝试使用居委会注册接口 - 用户名: {}, 用户类型: {}", 
                           request.getUsername(), request.getUserType());
                return ApiResponse.error(400, "此接口仅支持居委会注册");
            }
            
            // 验证居委会必填字段
            logger.info("【居委会注册】开始验证居委会必填字段 - 用户名: {}", request.getUsername());
            if (request.getCommitteeDesc() == null || request.getCommitteeDesc().trim().isEmpty()) {
                logger.warn("【居委会注册】居委会描述为空 - 用户名: {}", request.getUsername());
                return ApiResponse.error(400, "居委会描述不能为空");
            }
            if (request.getContact() == null || request.getContact().trim().isEmpty()) {
                logger.warn("【居委会注册】联系电话为空 - 用户名: {}", request.getUsername());
                return ApiResponse.error(400, "联系电话不能为空");
            }
            
            logger.info("【居委会注册】字段验证通过，开始注册 - 用户名: {}, 居委会描述: {}, 联系电话: {}", 
                       request.getUsername(), request.getCommitteeDesc(), request.getContact());
            
            // 调用统一的注册方法
            SimpleRegisterResponse response = userService.register(request);
            if (response.getMessage().contains("成功")) {
                logger.info("【居委会注册】注册成功 - 用户名: {}, 用户ID: {}, 待审批状态", 
                           request.getUsername());
                logger.debug("【居委会注册】注册响应详情: {}", response.toString());
                return ApiResponse.success("居委会注册成功，请等待管理员审批", response);
            } else {
                logger.warn("【居委会注册】注册失败 - 用户名: {}, 错误原因: {}", 
                           request.getUsername(), response.getMessage());
                return ApiResponse.error(400, response.getMessage());
            }
        } catch (Exception e) {
            logger.error("【居委会注册】注册异常 - 用户名: {}, 错误: {}", 
                        request.getUsername(), e.getMessage(), e);
            return ApiResponse.error("居委会注册失败：" + e.getMessage());
        }
    }
    
    /**
     * 居委会登录
     * 
     * @param request 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody SimpleLoginRequest request) {
        logger.info("【居委会登录】收到登录请求 - 用户名: {}, 用户类型: {}", 
                   request.getUsername(), request.getUserType());
        logger.debug("【居委会登录】请求详情: {}", request.toString());
        
        try {
            // 确保只能居委会登录
            if (!"committee".equals(request.getUserType())) {
                logger.warn("【居委会登录】非居委会用户尝试使用居委会登录接口 - 用户名: {}, 用户类型: {}", 
                           request.getUsername(), request.getUserType());
                return ApiResponse.error(400, "此接口仅支持居委会登录");
            }
            
            logger.info("【居委会登录】开始验证居委会身份 - 用户名: {}", request.getUsername());
            LoginResponse response = userService.login(request);
            
            if (response.getSuccess()) {
                logger.info("【居委会登录】登录成功 - 用户名: {}, 用户ID: {}", 
                           request.getUsername(), response.getId());
                logger.debug("【居委会登录】登录响应详情: {}", response.toString());
                return ApiResponse.success("居委会登录成功", response);
            } else {
                logger.warn("【居委会登录】登录失败 - 用户名: {}, 错误原因: {}", 
                           request.getUsername(), response.getReason());
                return ApiResponse.error(400, response.getReason());
            }
        } catch (Exception e) {
            logger.error("【居委会登录】登录异常 - 用户名: {}, 错误: {}", 
                        request.getUsername(), e.getMessage(), e);
            return ApiResponse.error("居委会登录失败：" + e.getMessage());
        }
    }
} 