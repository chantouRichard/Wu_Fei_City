-- 测试创建活动功能的SQL验证脚本
-- 运行此脚本来验证创建活动接口的数据库操作

-- 1. 查看当前活动总数
SELECT '=== 当前活动总数 ===' as info;
SELECT COUNT(*) as total_activities FROM activities;

-- 2. 查看最近创建的5个活动
SELECT '=== 最近创建的5个活动 ===' as info;
SELECT 
    id,
    title,
    created_at_and_signup_start_time as created_time,
    signup_end_time_and_activity_start_time as start_time,
    activity_end_time as end_time,
    location,
    status,
    organizer_id
FROM activities 
ORDER BY created_at_and_signup_start_time DESC 
LIMIT 5;

-- 3. 验证时间逻辑约束
SELECT '=== 验证活动时间逻辑 ===' as info;
SELECT 
    id,
    title,
    CASE 
        WHEN created_at_and_signup_start_time < signup_end_time_and_activity_start_time 
         AND signup_end_time_and_activity_start_time < activity_end_time 
        THEN '✓ 时间逻辑正确'
        ELSE '✗ 时间逻辑错误'
    END as time_check,
    created_at_and_signup_start_time,
    signup_end_time_and_activity_start_time,
    activity_end_time
FROM activities
ORDER BY created_at_and_signup_start_time DESC
LIMIT 10;

-- 4. 查看开放报名的活动（模拟API查询逻辑）
SELECT '=== 开放报名的活动 ===' as info;
SELECT 
    id,
    title,
    created_at_and_signup_start_time,
    signup_end_time_and_activity_start_time,
    activity_end_time,
    status,
    CASE 
        WHEN created_at_and_signup_start_time < NOW() 
         AND signup_end_time_and_activity_start_time > NOW() 
         AND status != 'finished' 
        THEN 'open'
        WHEN signup_end_time_and_activity_start_time < NOW() 
         AND activity_end_time > NOW() 
         AND status != 'finished' 
        THEN 'inprogress'
        WHEN activity_end_time < NOW() OR status = 'finished' 
        THEN 'finished'
        ELSE 'pending'
    END as computed_status
FROM activities 
WHERE created_at_and_signup_start_time < NOW() 
  AND signup_end_time_and_activity_start_time > NOW() 
  AND status != 'finished'
  AND status = 'published'
ORDER BY created_at_and_signup_start_time DESC;

-- 5. 查看进行中的活动
SELECT '=== 进行中的活动 ===' as info;
SELECT 
    id,
    title,
    signup_end_time_and_activity_start_time,
    activity_end_time,
    status,
    'inprogress' as computed_status
FROM activities 
WHERE signup_end_time_and_activity_start_time < NOW() 
  AND activity_end_time > NOW()
  AND status != 'finished'
  AND status = 'published'
ORDER BY signup_end_time_and_activity_start_time DESC;

-- 6. 查看已结束的活动
SELECT '=== 已结束的活动 ===' as info;
SELECT 
    id,
    title,
    activity_end_time,
    status,
    'finished' as computed_status
FROM activities 
WHERE activity_end_time < NOW() OR status = 'finished'
ORDER BY updated_at DESC;

-- 7. 检查新创建活动的组织者信息
SELECT '=== 活动组织者信息 ===' as info;
SELECT 
    a.id as activity_id,
    a.title,
    a.organizer_id,
    u.username,
    u.nickname,
    u.role
FROM activities a
LEFT JOIN users u ON a.organizer_id = u.id
ORDER BY a.created_at_and_signup_start_time DESC
LIMIT 5;

-- 8. 验证数据库约束是否生效
SELECT '=== 数据库约束验证 ===' as info;
SHOW CREATE TABLE activities;

-- 9. 查看活动参与情况（如果有的话）
SELECT '=== 活动参与情况 ===' as info;
SELECT 
    a.id as activity_id,
    a.title,
    COUNT(ap.user_id) as participant_count,
    a.max_participants
FROM activities a
LEFT JOIN activity_participants ap ON a.id = ap.activity_id
GROUP BY a.id, a.title, a.max_participants
ORDER BY a.created_at_and_signup_start_time DESC
LIMIT 10;

-- 10. 模拟创建活动后的状态检查
SELECT '=== 模拟新创建活动的状态检查 ===' as info;
-- 假设新创建的活动title包含"测试"或"社区清洁日"
SELECT 
    id,
    title,
    location,
    status,
    created_at_and_signup_start_time,
    signup_end_time_and_activity_start_time,
    activity_end_time,
    TIMESTAMPDIFF(HOUR, NOW(), signup_end_time_and_activity_start_time) as hours_until_start,
    TIMESTAMPDIFF(HOUR, signup_end_time_and_activity_start_time, activity_end_time) as activity_duration_hours
FROM activities 
WHERE title LIKE '%测试%' OR title LIKE '%社区清洁日%' OR title LIKE '%清洁%'
ORDER BY created_at_and_signup_start_time DESC;

-- 输出总结信息
SELECT '=== 测试总结 ===' as info;
SELECT 
    '活动总数' as metric,
    COUNT(*) as value
FROM activities
UNION ALL
SELECT 
    '开放报名活动数' as metric,
    COUNT(*) as value
FROM activities 
WHERE created_at_and_signup_start_time < NOW() 
  AND signup_end_time_and_activity_start_time > NOW() 
  AND status = 'published'
UNION ALL
SELECT 
    '进行中活动数' as metric,
    COUNT(*) as value
FROM activities 
WHERE signup_end_time_and_activity_start_time < NOW() 
  AND activity_end_time > NOW()
  AND status = 'published'
UNION ALL
SELECT 
    '已结束活动数' as metric,
    COUNT(*) as value
FROM activities 
WHERE activity_end_time < NOW() OR status = 'finished'; 