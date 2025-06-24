package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.dto.ApiResponse;
import com.whu.wufeibackend.dto.RankItem;
import com.whu.wufeibackend.entity.GreenScoreRecord;
import com.whu.wufeibackend.service.GreenScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 积分管理Controller
 * 提供积分记录、查询、排行榜等功能
 * 
 * @author 无废技术组
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/score")
@CrossOrigin(origins = "*")
public class ScoreController {
    
    private static final Logger logger = LoggerFactory.getLogger(ScoreController.class);
    
    @Autowired
    private GreenScoreService greenScoreService;
    
    /**
     * 添加积分记录
     * 
     * @param requestBody 请求体，包含userId、score、actionType、description
     * @return 添加结果
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addScore(@RequestBody Map<String, Object> requestBody) {
        logger.info("【积分添加】收到积分添加请求");
        logger.debug("【积分添加】请求参数: {}", requestBody);
        
        try {
            Integer userId = (Integer) requestBody.get("userId");
            Integer score = (Integer) requestBody.get("score");
            String actionType = (String) requestBody.get("actionType");
            String description = (String) requestBody.get("description");
            
            logger.info("【积分添加】解析参数 - userId: {}, score: {}, actionType: {}, description: {}", 
                       userId, score, actionType, description);
            
            if (userId == null || score == null || actionType == null) {
                logger.warn("【积分添加】参数不完整 - userId: {}, score: {}, actionType: {}", 
                           userId, score, actionType);
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, "参数不完整", null, false));
            }
            
            logger.info("【积分添加】开始添加积分记录 - userId: {}, score: {}, actionType: {}", 
                       userId, score, actionType);
            boolean success = greenScoreService.addScoreRecord(userId, score, actionType, description);
            
            if (success) {
                logger.info("【积分添加】积分记录添加成功 - userId: {}, score: {}, actionType: {}", 
                           userId, score, actionType);
                return ResponseEntity.ok(new ApiResponse<>(200, "积分记录添加成功", null, true));
            } else {
                logger.warn("【积分添加】积分记录添加失败 - userId: {}, score: {}, actionType: {}", 
                           userId, score, actionType);
                return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500, "积分记录添加失败", null, false));
            }
            
        } catch (Exception e) {
            logger.error("【积分添加】积分添加异常 - 错误: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                .body(new ApiResponse<>(500, "服务器内部错误", null, false));
        }
    }
    
    /**
     * 获取用户积分记录
     * 
     * @param userId 用户ID
     * @return 积分记录列表
     */
    @GetMapping("/records/{userId}")
    public ResponseEntity<ApiResponse<List<GreenScoreRecord>>> getUserScoreRecords(@PathVariable Integer userId) {
        logger.info("【获取积分记录】收到请求 - userId: {}", userId);
        
        try {
            logger.info("【获取积分记录】开始查询用户积分记录 - userId: {}", userId);
            List<GreenScoreRecord> records = greenScoreService.getUserScoreRecords(userId);
            
            logger.info("【获取积分记录】查询成功 - userId: {}, 记录数量: {}", userId, records.size());
            logger.debug("【获取积分记录】记录详情: {}", records);
            
            return ResponseEntity.ok(new ApiResponse<>(200, "获取积分记录成功", records, true));
        } catch (Exception e) {
            logger.error("【获取积分记录】查询异常 - userId: {}, 错误: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(500)
                .body(new ApiResponse<>(500, "获取积分记录失败", null, false));
        }
    }
    
    /**
     * 获取用户总积分
     * 
     * @param userId 用户ID
     * @return 总积分
     */
    @GetMapping("/total/{userId}")
    public ResponseEntity<ApiResponse<Integer>> getUserTotalScore(@PathVariable Integer userId) {
        logger.info("【获取总积分】收到请求 - userId: {}", userId);
        
        try {
            logger.info("【获取总积分】开始查询用户总积分 - userId: {}", userId);
            Integer totalScore = greenScoreService.getUserTotalScore(userId);
            
            logger.info("【获取总积分】查询成功 - userId: {}, 总积分: {}", userId, totalScore);
            
            return ResponseEntity.ok(new ApiResponse<>(200, "获取总积分成功", totalScore, true));
        } catch (Exception e) {
            logger.error("【获取总积分】查询异常 - userId: {}, 错误: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(500)
                .body(new ApiResponse<>(500, "获取总积分失败", null, false));
        }
    }
    
    /**
     * 获取排行榜
     * 
     * @return 排行榜列表（前10名）
     */
    @GetMapping("/ranking")
    public ResponseEntity<ApiResponse<List<RankItem>>> getRanking() {
        logger.info("【获取排行榜】收到请求");
        
        try {
            logger.info("【获取排行榜】开始查询排行榜");
            List<RankItem> ranking = greenScoreService.getTop10Ranking();
            
            logger.info("【获取排行榜】查询成功 - 排行榜条目数: {}", ranking.size());
            logger.debug("【获取排行榜】排行榜详情: {}", ranking);
            
            return ResponseEntity.ok(new ApiResponse<>(200, "获取排行榜成功", ranking, true));
        } catch (Exception e) {
            logger.error("【获取排行榜】查询异常 - 错误: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                .body(new ApiResponse<>(500, "获取排行榜失败", null, false));
        }
    }
    
    /**
     * 获取用户积分历史
     * 
     * @param userId 用户ID
     * @return 最近21天的积分历史
     */
    @GetMapping("/history/{userId}")
    public ResponseEntity<ApiResponse<List<Integer>>> getUserScoreHistory(@PathVariable Integer userId) {
        logger.info("【获取积分历史】收到请求 - userId: {}", userId);
        
        try {
            logger.info("【获取积分历史】开始查询用户积分历史 - userId: {}", userId);
            List<Integer> history = greenScoreService.getUserScoreHistory(userId);
            
            logger.info("【获取积分历史】查询成功 - userId: {}, 历史天数: {}", userId, history.size());
            logger.debug("【获取积分历史】历史详情: {}", history);
            
            return ResponseEntity.ok(new ApiResponse<>(200, "获取积分历史成功", history, true));
        } catch (Exception e) {
            logger.error("【获取积分历史】查询异常 - userId: {}, 错误: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(500)
                .body(new ApiResponse<>(500, "获取积分历史失败", null, false));
        }
    }
    
    /**
     * 快速添加常见行为积分
     * 
     * @param userId 用户ID
     * @param actionType 行为类型
     * @return 添加结果
     */
    @PostMapping("/quick-add/{userId}/{actionType}")
    public ResponseEntity<ApiResponse<String>> quickAddScore(@PathVariable Integer userId, @PathVariable String actionType) {
        logger.info("【快速添加积分】收到请求 - userId: {}, actionType: {}", userId, actionType);
        
        try {
            Integer score;
            String description;
            
            // 根据行为类型设置积分和描述
            logger.info("【快速添加积分】开始解析行为类型 - actionType: {}", actionType);
            switch (actionType) {
                case "recycling":
                    score = GreenScoreService.ScoreValue.RECYCLING;
                    description = "垃圾分类回收";
                    break;
                case "energy_saving":
                    score = GreenScoreService.ScoreValue.ENERGY_SAVING;
                    description = "节能减排行为";
                    break;
                case "activity_participation":
                    score = GreenScoreService.ScoreValue.ACTIVITY_PARTICIPATION;
                    description = "参与环保活动";
                    break;
                case "daily_check_in":
                    score = GreenScoreService.ScoreValue.DAILY_CHECK_IN;
                    description = "每日签到";
                    break;
                case "green_travel":
                    score = GreenScoreService.ScoreValue.GREEN_TRAVEL;
                    description = "绿色出行";
                    break;
                default:
                    logger.warn("【快速添加积分】不支持的行为类型 - userId: {}, actionType: {}", userId, actionType);
                    return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(400, "不支持的行为类型", null, false));
            }
            
            logger.info("【快速添加积分】行为类型解析成功 - userId: {}, actionType: {}, score: {}, description: {}", 
                       userId, actionType, score, description);
            
            boolean success = greenScoreService.addScoreRecord(userId, score, actionType, description);
            
            if (success) {
                logger.info("【快速添加积分】积分添加成功 - userId: {}, actionType: {}, score: {}", 
                           userId, actionType, score);
                return ResponseEntity.ok(new ApiResponse<>(200, "积分添加成功，+" + score + "分", null, true));
            } else {
                logger.warn("【快速添加积分】积分添加失败 - userId: {}, actionType: {}, score: {}", 
                           userId, actionType, score);
                return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500, "积分添加失败", null, false));
            }
            
        } catch (Exception e) {
            logger.error("【快速添加积分】积分添加异常 - userId: {}, actionType: {}, 错误: {}", 
                        userId, actionType, e.getMessage(), e);
            return ResponseEntity.status(500)
                .body(new ApiResponse<>(500, "服务器内部错误", null, false));
        }
    }
} 