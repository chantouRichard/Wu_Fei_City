-- ======================================
-- 数据库清理脚本 - 无废城市积分系统
-- 创建时间: 2024-06-27
-- 更新时间: 2025-01-27
-- ======================================
-- ⚠️  警告：此脚本会删除所有现有数据和表结构！
-- 🔒 建议在执行前先备份重要数据
-- 📋 执行后需要运行 init.sql 重新初始化数据库
-- ======================================

USE wu_fei_city;

-- ==========================================
-- 第一步：显示清理前的数据库状态
-- ==========================================
SELECT '=== 开始数据库清理 ===' AS status;
SELECT '正在清理数据库: wu_fei_city' AS database_name;

-- 显示当前所有表
SELECT 
    TABLE_NAME as existing_tables,
    TABLE_TYPE as table_type,
    TABLE_ROWS as estimated_rows
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'wu_fei_city'
ORDER BY TABLE_NAME;

-- ==========================================
-- 第二步：删除所有视图（必须先删除，因为它们依赖于表）
-- ==========================================
SELECT '=== 删除视图 ===' AS step;

-- 删除用户积分排名视图
DROP VIEW IF EXISTS user_score_ranking;
SELECT '✓ 删除视图: user_score_ranking' AS view_cleanup;

-- 删除其他可能存在的视图
DROP VIEW IF EXISTS activity_statistics;
DROP VIEW IF EXISTS user_activity_summary;
DROP VIEW IF EXISTS green_score_summary;

-- ==========================================
-- 第三步：删除所有表（按外键依赖关系倒序删除）
-- ==========================================
SELECT '=== 删除数据表 ===' AS step;

-- 删除活动参与记录表（有外键依赖activities和users）
DROP TABLE IF EXISTS activity_participants;
SELECT '✓ 删除表: activity_participants' AS table_cleanup;

-- 删除活动表（有外键依赖users）
DROP TABLE IF EXISTS activities;
SELECT '✓ 删除表: activities' AS table_cleanup;

-- 删除绿色积分记录表（有外键依赖users）
DROP TABLE IF EXISTS green_score_records;
SELECT '✓ 删除表: green_score_records' AS table_cleanup;

-- 删除用户表（主表，最后删除）
DROP TABLE IF EXISTS users;
SELECT '✓ 删除表: users' AS table_cleanup;

-- 删除可能存在的备份表
DROP TABLE IF EXISTS activities_backup;
DROP TABLE IF EXISTS activities_backup_20250127;
DROP TABLE IF EXISTS users_backup;
DROP TABLE IF EXISTS green_score_records_backup;
SELECT '✓ 删除备份表（如果存在）' AS backup_cleanup;

-- ==========================================
-- 第四步：清理其他数据库对象
-- ==========================================
SELECT '=== 清理其他数据库对象 ===' AS step;

-- 删除可能存在的存储过程
DROP PROCEDURE IF EXISTS calculate_user_score;
DROP PROCEDURE IF EXISTS update_activity_status;
DROP PROCEDURE IF EXISTS cleanup_old_records;

-- 删除可能存在的函数
DROP FUNCTION IF EXISTS get_user_total_score;
DROP FUNCTION IF EXISTS get_activity_participant_count;

-- 删除可能存在的触发器
DROP TRIGGER IF EXISTS tr_update_user_score;
DROP TRIGGER IF EXISTS tr_activity_status_change;

SELECT '✓ 清理存储过程、函数和触发器' AS objects_cleanup;

-- ==========================================
-- 第五步：验证清理结果
-- ==========================================
SELECT '=== 验证清理结果 ===' AS step;

-- 检查剩余的表
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN '✅ 所有表已成功删除'
        ELSE CONCAT('⚠️  仍有 ', COUNT(*), ' 个表未删除')
    END AS tables_status,
    COUNT(*) as remaining_tables
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'wu_fei_city';

-- 显示剩余的表（如果有）
SELECT 
    TABLE_NAME as remaining_table_name,
    TABLE_TYPE as table_type
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'wu_fei_city'
ORDER BY TABLE_NAME;

-- 检查剩余的视图
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN '✅ 所有视图已成功删除'
        ELSE CONCAT('⚠️  仍有 ', COUNT(*), ' 个视图未删除')
    END AS views_status
FROM information_schema.VIEWS 
WHERE TABLE_SCHEMA = 'wu_fei_city';

-- ==========================================
-- 第六步：可选操作 - 完全删除数据库
-- ==========================================
-- 如果需要完全删除数据库，请取消注释下面的语句
-- 注意：这将删除整个数据库，包括数据库本身！

/*
DROP DATABASE IF EXISTS wu_fei_city;
SELECT '✅ 数据库 wu_fei_city 已完全删除' AS database_cleanup;
*/

-- ==========================================
-- 清理完成总结
-- ==========================================
SELECT '==========================================' AS separator;
SELECT '🎉 数据库清理完成！' AS final_status;
SELECT '📊 以下对象已被清理：' AS summary_title;
SELECT '   • 所有数据表 (users, activities, activity_participants, green_score_records)' AS cleaned_tables;
SELECT '   • 所有视图 (user_score_ranking 等)' AS cleaned_views;
SELECT '   • 所有备份表' AS cleaned_backups;
SELECT '   • 存储过程、函数和触发器' AS cleaned_objects;
SELECT '==========================================' AS separator;
SELECT '📋 下一步操作：' AS next_steps_title;
SELECT '1. 运行 init.sql 重新初始化数据库结构' AS step1;
SELECT '2. 运行 spring boot 应用进行测试' AS step2;
SELECT '3. 使用 Postman 验证 API 接口' AS step3;
SELECT '==========================================' AS separator;

-- 记录清理时间
SELECT NOW() AS cleanup_completed_at; 