package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.dto.ApiResponse;
import com.whu.wufeibackend.dto.LoginResponse;
import com.whu.wufeibackend.dto.SimpleLoginRequest;
import com.whu.wufeibackend.entity.User;
import com.whu.wufeibackend.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员控制器
 * 处理管理员相关的功能：登录、审批居委会、系统管理等，需要JWT令牌验证
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * 管理员登录
     * 
     * @param request 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody SimpleLoginRequest request) {
        logger.info("【管理员登录】收到登录请求 - 用户名: {}, 用户类型: {}", 
                   request.getUsername(), request.getUserType());
        logger.debug("【管理员登录】请求详情: {}", request.toString());
        
        try {
            // 确保只能管理员登录
            if (!"admin".equals(request.getUserType())) {
                logger.warn("【管理员登录】非管理员尝试访问管理员接口 - 用户名: {}, 用户类型: {}", 
                           request.getUsername(), request.getUserType());
                return ApiResponse.error(400, "此接口仅支持管理员登录");
            }
            
            logger.info("【管理员登录】开始验证管理员身份 - 用户名: {}", request.getUsername());
            LoginResponse response = userService.login(request);
            
            if (response.getSuccess()) {
                logger.info("【管理员登录】管理员登录成功 - 用户名: {}, 用户ID: {}", 
                           request.getUsername(), response.getId());
                logger.debug("【管理员登录】登录响应详情: {}", response.toString());
                return ApiResponse.success("管理员登录成功", response);
            } else {
                logger.warn("【管理员登录】管理员登录失败 - 用户名: {}, 错误原因: {}", 
                           request.getUsername(), response.getReason());
                return ApiResponse.error(400, response.getReason());
            }
        } catch (Exception e) {
            logger.error("【管理员登录】管理员登录异常 - 用户名: {}, 错误: {}", 
                        request.getUsername(), e.getMessage(), e);
            return ApiResponse.error("管理员登录失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取待审批的居委会用户列表
     * 
     * @param token JWT令牌
     * @return 待审批用户列表
     */
    @GetMapping("/pending-users")
    public ApiResponse<List<User>> getPendingUsers(@RequestHeader("Authorization") String token) {
        logger.info("【获取待审批列表】收到请求");
        logger.debug("【获取待审批列表】收到前端token: {}", token);
        
        // 自动去掉Bearer前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            logger.debug("【获取待审批列表】去前缀后token: {}", token);
        }
        
        try {
            // 验证JWT令牌
            logger.info("【获取待审批列表】开始验证JWT令牌");
            if (!userService.validateToken(token)) {
                logger.warn("【获取待审批列表】JWT令牌验证失败");
                return ApiResponse.error(401, "无效的令牌");
            }
            
            // 验证是否为管理员
            logger.info("【获取待审批列表】开始验证管理员权限");
            String userType = userService.getUserTypeFromToken(token);
            logger.debug("【获取待审批列表】从token中获取的用户类型: {}", userType);
            
            if (!"admin".equals(userType)) {
                logger.warn("【获取待审批列表】非管理员尝试访问 - 用户类型: {}", userType);
                return ApiResponse.error(403, "权限不足");
            }
            
            logger.info("【获取待审批列表】开始查询待审批用户");
            List<User> pendingUsers = userService.getPendingCommitteeUsers();
            logger.info("【获取待审批列表】查询成功 - 待审批用户数量: {}", pendingUsers.size());
            logger.debug("【获取待审批列表】待审批用户详情: {}", pendingUsers.toString());
            
            return ApiResponse.success("获取待审批用户列表成功", pendingUsers);
        } catch (Exception e) {
            logger.error("【获取待审批列表】操作异常 - 错误: {}", e.getMessage(), e);
            return ApiResponse.error("获取待审批用户列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 审批居委会用户
     * 
     * @param token JWT令牌
     * @param payload 审批请求
     * @return 审批结果
     */
    @PostMapping("/approve")
    public ApiResponse<?> approveUser(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> payload) {
        logger.info("【用户审批】收到审批请求");
        logger.debug("【用户审批】收到前端token: {}", token);
        logger.debug("【用户审批】收到payload: {}", payload);
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            logger.debug("【用户审批】去前缀后token: {}", token);
        }
        
        try {
            // 校验token
            logger.info("【用户审批】开始验证JWT令牌");
            boolean valid = userService.validateToken(token);
            logger.debug("【用户审批】token校验结果: {}", valid);
            
            if (!valid) {
                logger.warn("【用户审批】JWT令牌验证失败");
                return ApiResponse.error(401, "无效的令牌");
            }
            
            // 解析参数
            Integer userId = null;
            Boolean approved = null;
            try {
                userId = (payload.get("userId") instanceof Integer) ? 
                        (Integer) payload.get("userId") : 
                        Integer.parseInt(payload.get("userId").toString());
                approved = (Boolean) payload.get("approved");
                
                logger.info("【用户审批】解析参数成功 - userId: {}, approved: {}", userId, approved);
            } catch (Exception e) {
                logger.error("【用户审批】参数解析异常 - 错误: {}", e.getMessage(), e);
                return ApiResponse.error(400, "参数解析失败：" + e.getMessage());
            }
            
            // 审批业务
            logger.info("【用户审批】开始执行审批业务 - userId: {}, approved: {}", userId, approved);
            boolean result = userService.approveCommitteeUser(userId, approved != null && approved);
            logger.info("【用户审批】审批业务执行完成 - 结果: {}", result);
            
            if (result) {
                String action = (approved != null && approved) ? "通过" : "拒绝";
                logger.info("【用户审批】审批{}成功 - userId: {}", action, userId);
                return ApiResponse.success("审批" + action + "成功");
            } else {
                logger.warn("【用户审批】审批失败 - userId: {}, 可能原因: 用户不存在或非居委会用户", userId);
                return ApiResponse.error(500, "审批失败");
            }
        } catch (Exception e) {
            logger.error("【用户审批】审批异常 - 错误: {}", e.getMessage(), e);
            return ApiResponse.error(500, "审批失败：" + e.getMessage());
        }
    }
    
    /**
     * 批量审批居委会用户
     * 
     * @param token JWT令牌
     * @param payload 批量审批请求，包含用户ID列表和审批结果
     * @return 批量审批结果
     */
    @PostMapping("/batch-approve")
    public ApiResponse<Map<String, Object>> batchApproveUsers(
            @RequestHeader("Authorization") String token, 
            @RequestBody Map<String, Object> payload) {
        logger.info("【批量审批】收到批量审批请求");
        logger.debug("【批量审批】收到payload: {}", payload);
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            // 验证JWT令牌和管理员权限
            if (!userService.validateToken(token)) {
                return ApiResponse.error(401, "无效的令牌");
            }
            
            String userType = userService.getUserTypeFromToken(token);
            if (!"admin".equals(userType)) {
                return ApiResponse.error(403, "权限不足");
            }
            
            // 解析批量审批参数
            @SuppressWarnings("unchecked")
            List<Integer> userIds = (List<Integer>) payload.get("userIds");
            Boolean approved = (Boolean) payload.get("approved");
            
            if (userIds == null || userIds.isEmpty()) {
                return ApiResponse.error(400, "用户ID列表不能为空");
            }
            
            if (approved == null) {
                return ApiResponse.error(400, "审批结果不能为空");
            }
            
            logger.info("【批量审批】开始执行批量审批 - 用户数量: {}, 审批结果: {}", userIds.size(), approved);
            
            // 执行批量审批
            int successCount = 0;
            int failCount = 0;
            StringBuilder failedUsers = new StringBuilder();
            
            for (Integer userId : userIds) {
                try {
                    boolean result = userService.approveCommitteeUser(userId, approved);
                    if (result) {
                        successCount++;
                    } else {
                        failCount++;
                        if (failedUsers.length() > 0) failedUsers.append(", ");
                        failedUsers.append(userId);
                    }
                } catch (Exception e) {
                    failCount++;
                    if (failedUsers.length() > 0) failedUsers.append(", ");
                    failedUsers.append(userId);
                    logger.error("【批量审批】审批用户{}失败: {}", userId, e.getMessage());
                }
            }
            
            // 构建返回结果
            Map<String, Object> result = Map.of(
                "totalCount", userIds.size(),
                "successCount", successCount,
                "failCount", failCount,
                "failedUserIds", failedUsers.toString(),
                "approved", approved
            );
            
            String action = approved ? "通过" : "拒绝";
            String message = String.format("批量审批完成：%d个成功，%d个失败", successCount, failCount);
            
            logger.info("【批量审批】批量审批{}完成 - 成功: {}, 失败: {}", action, successCount, failCount);
            
            return ApiResponse.success(message, result);
            
        } catch (Exception e) {
            logger.error("【批量审批】批量审批异常 - 错误: {}", e.getMessage(), e);
            return ApiResponse.error("批量审批失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取审批统计信息
     * 
     * @param token JWT令牌
     * @return 审批统计数据
     */
    @GetMapping("/approval-stats")
    public ApiResponse<Map<String, Object>> getApprovalStats(@RequestHeader("Authorization") String token) {
        logger.info("【审批统计】收到统计请求");
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            // 验证JWT令牌和管理员权限
            if (!userService.validateToken(token)) {
                return ApiResponse.error(401, "无效的令牌");
            }
            
            String userType = userService.getUserTypeFromToken(token);
            if (!"admin".equals(userType)) {
                return ApiResponse.error(403, "权限不足");
            }
            
            logger.info("【审批统计】开始查询统计数据");
            
            // 获取各种状态的统计数据
            List<User> pendingUsers = userService.getPendingCommitteeUsers();
            List<User> approvedUsers = userService.getApprovedCommitteeUsers();
            List<User> rejectedUsers = userService.getRejectedCommitteeUsers();
            
            Map<String, Object> stats = Map.of(
                "pendingCount", pendingUsers.size(),
                "approvedCount", approvedUsers.size(),
                "rejectedCount", rejectedUsers.size(),
                "totalCount", pendingUsers.size() + approvedUsers.size() + rejectedUsers.size()
            );
            
            logger.info("【审批统计】统计查询成功 - 待审批: {}, 已通过: {}, 已拒绝: {}", 
                       pendingUsers.size(), approvedUsers.size(), rejectedUsers.size());
            
            return ApiResponse.success("获取审批统计成功", stats);
            
        } catch (Exception e) {
            logger.error("【审批统计】统计查询异常 - 错误: {}", e.getMessage(), e);
            return ApiResponse.error("获取审批统计失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取已审批的居委会用户列表（包括通过和拒绝的）
     * 
     * @param token JWT令牌
     * @param status 审批状态：approved(已通过) 或 rejected(已拒绝)
     * @return 已审批用户列表
     */
    @GetMapping("/approved-users")
    public ApiResponse<List<User>> getApprovedUsers(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "status", defaultValue = "approved") String status) {
        logger.info("【获取已审批列表】收到请求 - 状态: {}", status);
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            // 验证JWT令牌和管理员权限
            if (!userService.validateToken(token)) {
                return ApiResponse.error(401, "无效的令牌");
            }
            
            String userType = userService.getUserTypeFromToken(token);
            if (!"admin".equals(userType)) {
                return ApiResponse.error(403, "权限不足");
            }
            
            List<User> users;
            String message;
            
            if ("approved".equals(status)) {
                logger.info("【获取已审批列表】查询已通过的居委会用户");
                users = userService.getApprovedCommitteeUsers();
                message = "获取已通过的居委会用户成功";
            } else if ("rejected".equals(status)) {
                logger.info("【获取已审批列表】查询已拒绝的居委会用户");
                users = userService.getRejectedCommitteeUsers();
                message = "获取已拒绝的居委会用户成功";
            } else {
                return ApiResponse.error(400, "状态参数无效，支持：approved 或 rejected");
            }
            
            logger.info("【获取已审批列表】查询成功 - 状态: {}, 数量: {}", status, users.size());
            
            return ApiResponse.success(message, users);
            
        } catch (Exception e) {
            logger.error("【获取已审批列表】查询异常 - 错误: {}", e.getMessage(), e);
            return ApiResponse.error("获取已审批用户列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取特定用户的详细信息（用于审批时查看详情）
     * 
     * @param token JWT令牌
     * @param userId 用户ID
     * @return 用户详细信息
     */
    @GetMapping("/user-detail/{userId}")
    public ApiResponse<User> getUserDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer userId) {
        logger.info("【获取用户详情】收到请求 - userId: {}", userId);
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        try {
            // 验证JWT令牌和管理员权限
            if (!userService.validateToken(token)) {
                return ApiResponse.error(401, "无效的令牌");
            }
            
            String userType = userService.getUserTypeFromToken(token);
            if (!"admin".equals(userType)) {
                return ApiResponse.error(403, "权限不足");
            }
            
            logger.info("【获取用户详情】开始查询用户详情 - userId: {}", userId);
            User user = userService.getUserById(userId);
            
            if (user == null) {
                logger.warn("【获取用户详情】用户不存在 - userId: {}", userId);
                return ApiResponse.error(404, "用户不存在");
            }
            
            // 出于安全考虑，清除密码字段
            user.setPassword(null);
            
            logger.info("【获取用户详情】查询成功 - userId: {}, 用户类型: {}", userId, user.getUserType());
            
            return ApiResponse.success("获取用户详情成功", user);
            
        } catch (Exception e) {
            logger.error("【获取用户详情】查询异常 - userId: {}, 错误: {}", userId, e.getMessage(), e);
            return ApiResponse.error("获取用户详情失败：" + e.getMessage());
        }
    }
} 