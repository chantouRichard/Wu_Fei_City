package com.whu.wufeibackend.mapper;

import com.whu.wufeibackend.entity.GreenScoreRecord;
import com.whu.wufeibackend.entity.UserScoreRanking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 绿色积分相关的Mapper接口
 * 
 * @author 无废技术组
 * @since 2024-01-01
 */
@Mapper
public interface GreenScoreMapper {
    
    /**
     * 插入积分记录
     * 
     * @param record 积分记录
     * @return 影响行数
     */
    int insertScoreRecord(GreenScoreRecord record);
    
    /**
     * 根据用户ID查询积分记录
     * 
     * @param userId 用户ID
     * @return 积分记录列表
     */
    List<GreenScoreRecord> findByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据用户ID和日期范围查询积分记录
     * 
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 积分记录列表
     */
    List<GreenScoreRecord> findByUserIdAndDateRange(@Param("userId") Integer userId,
                                                    @Param("startDate") LocalDate startDate, 
                                                    @Param("endDate") LocalDate endDate);
    
    /**
     * 获取用户总积分
     * 
     * @param userId 用户ID
     * @return 总积分
     */
    Integer getUserTotalScore(@Param("userId") Integer userId);
    
    /**
     * 获取排行榜（前N名）
     * 
     * @param limit 限制数量
     * @return 排行榜列表
     */
    List<UserScoreRanking> getRankingList(@Param("limit") Integer limit);
    
    /**
     * 获取用户在排行榜中的排名信息
     * 
     * @param userId 用户ID
     * @return 用户排名信息
     */
    UserScoreRanking getUserRanking(@Param("userId") Integer userId);
    
    /**
     * 获取用户最近N天的积分历史
     * 
     * @param userId 用户ID
     * @return 每日积分数组（按日期顺序）
     */
    List<Integer> getUserScoreHistory(@Param("userId") Integer userId);
    
    /**
     * 获取用户参与活动的次数
     * 
     * @param userId 用户ID
     * @return 活动参与次数
     */
    Integer getUserActivityParticipationCount(@Param("userId") Integer userId);

    /**
     * 更新用户某天的绿植分数
     *
     * @return 修改结果
     */
    Integer updateGreenScoreById(GreenScoreRecord recore);
} 