package com.whu.wufeibackend.service;

import com.whu.wufeibackend.DTO.RankItem;
import com.whu.wufeibackend.entity.GreenScoreRecord;
import com.whu.wufeibackend.entity.UserScoreRanking;
import com.whu.wufeibackend.mapper.GreenScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 绿色积分服务类
 * 
 * @author 无废技术组
 * @since 2024-01-01
 */
@Service
public class GreenScoreService {
    
    @Autowired
    private GreenScoreMapper greenScoreMapper;
    
    /**
     * 添加积分记录
     * 
     * @param userId 用户ID
     * @param score 积分值
     * @param actionType 行为类型
     * @param description 描述
     * @return 是否成功
     */
    @Transactional
    public boolean addScoreRecord(Integer userId, Integer score, String actionType, String description) {
        try{
            List<GreenScoreRecord> record = greenScoreMapper.findByUserIdAndDateRange(userId, LocalDate.now(), LocalDate.now());
            if (!record.isEmpty()) {
                // 获取第一条记录
                GreenScoreRecord firstRecord = record.get(0);

                // 更新score字段（假设score是要增加的值）
                firstRecord.setScore(firstRecord.getScore() + score);

                // 调用更新方法
                return greenScoreMapper.updateGreenScoreById(firstRecord)==1;
            }
            else {
                GreenScoreRecord temp = new GreenScoreRecord(userId, score, actionType, description, LocalDate.now());
                int result = greenScoreMapper.insertScoreRecord(temp);
                return result > 0;
            }
        }catch (Exception e){
            // System.out.println(e);
            return false;
        }
    }
    
    /**
     * 获取用户总积分
     * 
     * @param userId 用户ID
     * @return 总积分
     */
    public Integer getUserTotalScore(Integer userId) {
        Integer totalScore = greenScoreMapper.getUserTotalScore(userId);
        return totalScore != null ? totalScore : 0;
    }
    
    /**
     * 获取排行榜前10名
     * 
     * @return 排行榜列表
     */
    public List<RankItem> getTop5Ranking() {
        List<UserScoreRanking> rankings = greenScoreMapper.getRankingList(5);
        return rankings.stream()
                .map(ranking -> {
                    RankItem item = new RankItem();
                    // 根据排名设置称号
                    item.setTitle(getRankTitle(ranking.getRank()));
                    item.setNickname(ranking.getNickname());
                    item.setGreenScore(ranking.getTotalScore());
                    return item;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 获取用户积分历史（最近21天）
     * 这里体现了"用这个表查到数据库后，在Service层去做处理"的设计思路
     * 
     * @param userId 用户ID
     * @return 积分历史数组，按日期顺序排列，共21个元素
     */
    public List<Integer> getUserScoreHistory(Integer userId) {
        try {
            // 获取当前日期和当月第一天
            LocalDate today = LocalDate.now();
            LocalDate firstDayOfMonth = today.withDayOfMonth(1);

            // 查询当月所有积分记录
            List<GreenScoreRecord> records = greenScoreMapper.findByUserIdAndDateRange(
                    userId, firstDayOfMonth, today);

            // 获取当月总天数
            int daysInMonth = today.lengthOfMonth();
            List<Integer> scoreLevels = new ArrayList<>();

            // 遍历每一天并填充档次
            for (int i = 0; i < daysInMonth; i++) {
                LocalDate currentDate = firstDayOfMonth.plusDays(i);
                GreenScoreRecord record = records.stream()
                        .filter(r -> r.getRecordDate().equals(currentDate))
                        .findFirst()
                        .orElse(null);

                Integer score = (record != null) ? record.getScore() : 0;

                // 划分档次
                Integer level;
                if(score >= 50){
                    level = 4;
                }
                else if(score >= 25){
                    level = 3;

                }
                else if (score >= 15) {
                    level = 2;
                } else if (score >= 5) {
                    level = 1;
                } else {
                    level = 0;
                }

                scoreLevels.add(level);
            }

            return scoreLevels;

        } catch (Exception e) {
            e.printStackTrace();
            // 异常情况下返回默认档次数组（全为 low）
            List<Integer> defaultLevels = new ArrayList<>();
            int daysInMonth = LocalDate.now().lengthOfMonth();
            for (int i = 0; i < daysInMonth; i++) {
                defaultLevels.add(0);
            }
            return defaultLevels;
        }
    }
    
    /**
     * 获取用户活动参与次数
     * 
     * @param userId 用户ID
     * @return 参与次数
     */
    public Integer getUserActivityParticipationCount(Integer userId) {
        Integer count = greenScoreMapper.getUserActivityParticipationCount(userId);
        return count != null ? count : 0;
    }
    
    /**
     * 获取用户积分记录
     * 
     * @param userId 用户ID
     * @return 积分记录列表
     */
    public List<GreenScoreRecord> getUserScoreRecords(Integer userId) {
        return greenScoreMapper.findByUserId(userId);
    }
    
    /**
     * 根据排名获取称号
     * 
     * @param rank 排名
     * @return 称号
     */
    private String getRankTitle(Integer rank) {
        if (rank == null) return "环保新人";
        
        switch (rank) {
            case 1: return "环保达人";
            case 2: return "绿色先锋";
            case 3: return "生态卫士";
            case 4: return "环保新星";
            case 5: return "绿色使者";
            case 6: return "节能专家";
            case 7: return "环保志愿者";
            case 8: return "减碳达人";
            case 9: return "回收高手";
            case 10: return "环保新人";
            default: return "环保爱好者";
        }
    }
    
    /**
     * 常见积分行为枚举
     */
    public static class ActionType {
        public static final String RECYCLING = "recycling";                    // 垃圾分类回收
        public static final String ENERGY_SAVING = "energy_saving";            // 节能减排
        public static final String ACTIVITY_PARTICIPATION = "activity_participation"; // 参与活动
        public static final String DAILY_CHECK_IN = "daily_check_in";          // 每日签到
        public static final String SHARE_KNOWLEDGE = "share_knowledge";        // 分享环保知识
        public static final String GREEN_TRAVEL = "green_travel";              // 绿色出行
        public static final String WATER_SAVING = "water_saving";              // 节约用水
        public static final String PAPER_SAVING = "paper_saving";              // 节约用纸
    }
    
    /**
     * 积分值常量
     */
    public static class ScoreValue {
        public static final int LEARNING = 1;                    // 垃圾分类回收 +10分
        public static final int ENERGY_SAVING = 15;                // 节能减排 +15分
        public static final int ACTIVITY_PARTICIPATION = 20;       // 参与活动 +20分
        public static final int DAILY_CHECK_IN = 5;                // 每日签到 +5分
        public static final int SHARE_KNOWLEDGE = 8;               // 分享环保知识 +8分
        public static final int GREEN_TRAVEL = 12;                 // 绿色出行 +12分
        public static final int WATER_SAVING = 6;                  // 节约用水 +6分
        public static final int PAPER_SAVING = 4;                  // 节约用纸 +4分
    }
    
    /**
     * 【示例方法】详细说明Service层数据处理过程
     * 展示"用green_score_records表查到数据库后，在Service层做处理"的完整流程
     * 
     * @param userId 用户ID
     * @return 用户积分统计信息
     */
    public UserScoreStatistics getUserScoreStatisticsExample(Integer userId) {
        // ====== 第1步：从数据库查询原始数据 ======
        // 使用green_score_records表查询所有积分记录
        List<GreenScoreRecord> allRecords = greenScoreMapper.findByUserId(userId);
        
        // ====== 第2步：在Service层进行各种业务处理 ======
        
        // 处理1：计算总积分
        int totalScore = allRecords.stream()
            .mapToInt(GreenScoreRecord::getScore)
            .sum();
        
        // 处理2：按行为类型分组统计
        java.util.Map<String, Integer> scoreByActionType = allRecords.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                GreenScoreRecord::getActionType,
                java.util.stream.Collectors.summingInt(GreenScoreRecord::getScore)
            ));
        
        // 处理3：计算最近7天的积分
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(6);
        int recentScore = allRecords.stream()
            .filter(record -> record.getRecordDate().isAfter(sevenDaysAgo) || 
                             record.getRecordDate().equals(sevenDaysAgo))
            .mapToInt(GreenScoreRecord::getScore)
            .sum();
        
        // 处理4：计算平均每日积分
        long totalDays = allRecords.stream()
            .map(GreenScoreRecord::getRecordDate)
            .distinct()
            .count();
        double avgDailyScore = totalDays > 0 ? (double) totalScore / totalDays : 0;
        
        // 处理5：找出最高单日积分
        int maxDailyScore = allRecords.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                GreenScoreRecord::getRecordDate,
                java.util.stream.Collectors.summingInt(GreenScoreRecord::getScore)
            ))
            .values()
            .stream()
            .mapToInt(Integer::intValue)
            .max()
            .orElse(0);
        
        // ====== 第3步：封装处理结果返回 ======
        UserScoreStatistics statistics = new UserScoreStatistics();
        statistics.setUserId(userId);
        statistics.setTotalScore(totalScore);
        statistics.setScoreByActionType(scoreByActionType);
        statistics.setRecentSevenDaysScore(recentScore);
        statistics.setAvgDailyScore(avgDailyScore);
        statistics.setMaxDailyScore(maxDailyScore);
        statistics.setTotalRecordDays((int) totalDays);
        
        return statistics;
    }
    
    /**
     * 用户积分统计信息类（示例）
     */
    public static class UserScoreStatistics {
        private Integer userId;
        private Integer totalScore;
        private java.util.Map<String, Integer> scoreByActionType;
        private Integer recentSevenDaysScore;
        private Double avgDailyScore;
        private Integer maxDailyScore;
        private Integer totalRecordDays;
        
        // Getters and Setters
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        
        public Integer getTotalScore() { return totalScore; }
        public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
        
        public java.util.Map<String, Integer> getScoreByActionType() { return scoreByActionType; }
        public void setScoreByActionType(java.util.Map<String, Integer> scoreByActionType) { this.scoreByActionType = scoreByActionType; }
        
        public Integer getRecentSevenDaysScore() { return recentSevenDaysScore; }
        public void setRecentSevenDaysScore(Integer recentSevenDaysScore) { this.recentSevenDaysScore = recentSevenDaysScore; }
        
        public Double getAvgDailyScore() { return avgDailyScore; }
        public void setAvgDailyScore(Double avgDailyScore) { this.avgDailyScore = avgDailyScore; }
        
        public Integer getMaxDailyScore() { return maxDailyScore; }
        public void setMaxDailyScore(Integer maxDailyScore) { this.maxDailyScore = maxDailyScore; }
        
        public Integer getTotalRecordDays() { return totalRecordDays; }
        public void setTotalRecordDays(Integer totalRecordDays) { this.totalRecordDays = totalRecordDays; }
    }
} 