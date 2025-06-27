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
            // 获取 userId 字符串
            Integer userId = (Integer) requestBody.get("userId");
            Integer score = (Integer) requestBody.get("score");
            String actionType = (String) requestBody.get("actionType");
            String description = (String) requestBody.get("description");
            
            if (userId == null || score == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, "参数不完整", null, false));
            }

            boolean success = greenScoreService.addScoreRecord(userId, score, actionType, description);
            
            if (success) {

                return ResponseEntity.ok(new ApiResponse<>(200, "积分记录添加成功", null, true));
            } else {

                return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500, "积分记录添加失败", null, false));
            }
            
        } catch (Exception e) {
            logger.info("错误eeee：",e);
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
            List<RankItem> ranking = greenScoreService.getTop5Ranking();
            
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
     * @return 当月的积分历史
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

} 