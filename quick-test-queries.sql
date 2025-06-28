-- ==========================================
-- 快速测试SQL查询脚本
-- 用于验证活动表字段变更和数据正确性
-- 使用方法：在MySQL Workbench中逐个执行以下查询
-- ==========================================

USE wu_fei_city;

-- ==========================================
-- 1. 表结构验证
-- ==========================================

-- 检查活动表是否存在
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✅ activities表存在'
        ELSE '❌ activities表不存在'
    END AS table_status
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'wu_fei_city' AND TABLE_NAME = 'activities';

-- 查看完整表结构
SELECT 
    COLUMN_NAME AS '字段名',
    COLUMN_TYPE AS '数据类型',
    IS_NULLABLE AS '允许NULL',
    COLUMN_DEFAULT AS '默认值',
    COLUMN_COMMENT AS '注释'
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'wu_fei_city' 
  AND TABLE_NAME = 'activities'
ORDER BY ORDINAL_POSITION;

-- 检查新字段是否存在
SELECT 
    CASE 
        WHEN COUNT(*) = 3 THEN '✅ 新字段结构正确'
        ELSE CONCAT('❌ 缺少字段，当前数量: ', COUNT(*))
    END AS new_fields_status
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'wu_fei_city' 
  AND TABLE_NAME = 'activities' 
  AND COLUMN_NAME IN (
    'created_at_and_signup_start_time', 
    'signup_end_time_and_activity_start_time', 
    'activity_end_time'
  );

-- 检查旧字段是否已删除
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN '✅ 旧字段已删除'
        ELSE CONCAT('❌ 仍存在旧字段，数量: ', COUNT(*))
    END AS old_fields_status
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'wu_fei_city' 
  AND TABLE_NAME = 'activities' 
  AND COLUMN_NAME IN ('start_time', 'end_time', 'created_at');

-- ==========================================
-- 2. 数据内容验证
-- ==========================================

-- 查看活动总数
SELECT 
    COUNT(*) AS '总活动数',
    COUNT(CASE WHEN status = 'published' THEN 1 END) AS '已发布活动数',
    COUNT(CASE WHEN status = 'pending' THEN 1 END) AS '待发布活动数',
    COUNT(CASE WHEN status = 'finished' THEN 1 END) AS '已完成活动数'
FROM activities;

-- 查看所有活动的时间字段
SELECT 
    id AS '活动ID',
    title AS '活动标题',
    DATE_FORMAT(created_at_and_signup_start_time, '%Y-%m-%d %H:%i') AS '创建时间/报名开始',
    DATE_FORMAT(signup_end_time_and_activity_start_time, '%Y-%m-%d %H:%i') AS '报名结束/活动开始',
    DATE_FORMAT(activity_end_time, '%Y-%m-%d %H:%i') AS '活动结束时间',
    status AS '状态'
FROM activities
ORDER BY created_at_and_signup_start_time DESC;

-- ==========================================
-- 3. 业务逻辑验证
-- ==========================================

-- 验证活动状态分布（基于时间逻辑）
SELECT 
    CASE 
        WHEN created_at_and_signup_start_time < NOW() 
         AND signup_end_time_and_activity_start_time > NOW() 
         AND status != 'finished' THEN 'open'
        WHEN signup_end_time_and_activity_start_time < NOW() 
         AND activity_end_time > NOW() 
         AND status != 'finished' THEN 'inprogress'
        WHEN activity_end_time < NOW() 
         OR status = 'finished' THEN 'finished'
        ELSE 'pending'
    END AS '计算状态',
    COUNT(*) AS '活动数量',
    GROUP_CONCAT(title SEPARATOR ', ') AS '活动列表'
FROM activities
WHERE status = 'published'
GROUP BY 
    CASE 
        WHEN created_at_and_signup_start_time < NOW() 
         AND signup_end_time_and_activity_start_time > NOW() 
         AND status != 'finished' THEN 'open'
        WHEN signup_end_time_and_activity_start_time < NOW() 
         AND activity_end_time > NOW() 
         AND status != 'finished' THEN 'inprogress'
        WHEN activity_end_time < NOW() 
         OR status = 'finished' THEN 'finished'
        ELSE 'pending'
    END
ORDER BY 
    CASE 
        WHEN CASE 
            WHEN created_at_and_signup_start_time < NOW() 
             AND signup_end_time_and_activity_start_time > NOW() 
             AND status != 'finished' THEN 'open'
            WHEN signup_end_time_and_activity_start_time < NOW() 
             AND activity_end_time > NOW() 
             AND status != 'finished' THEN 'inprogress'
            WHEN activity_end_time < NOW() 
             OR status = 'finished' THEN 'finished'
            ELSE 'pending'
        END = 'open' THEN 1
        WHEN CASE 
            WHEN created_at_and_signup_start_time < NOW() 
             AND signup_end_time_and_activity_start_time > NOW() 
             AND status != 'finished' THEN 'open'
            WHEN signup_end_time_and_activity_start_time < NOW() 
             AND activity_end_time > NOW() 
             AND status != 'finished' THEN 'inprogress'
            WHEN activity_end_time < NOW() 
             OR status = 'finished' THEN 'finished'
            ELSE 'pending'
        END = 'inprogress' THEN 2
        WHEN CASE 
            WHEN created_at_and_signup_start_time < NOW() 
             AND signup_end_time_and_activity_start_time > NOW() 
             AND status != 'finished' THEN 'open'
            WHEN signup_end_time_and_activity_start_time < NOW() 
             AND activity_end_time > NOW() 
             AND status != 'finished' THEN 'inprogress'
            WHEN activity_end_time < NOW() 
             OR status = 'finished' THEN 'finished'
            ELSE 'pending'
        END = 'finished' THEN 3
        ELSE 4
    END;

-- 检查时间逻辑的合理性
SELECT 
    id,
    title,
    CASE 
        WHEN created_at_and_signup_start_time >= signup_end_time_and_activity_start_time 
        THEN '❌ 报名开始时间不能晚于或等于报名结束时间'
        WHEN signup_end_time_and_activity_start_time >= activity_end_time 
        THEN '❌ 活动开始时间不能晚于或等于活动结束时间'
        ELSE '✅ 时间逻辑正确'
    END AS '时间逻辑检查'
FROM activities
WHERE status = 'published';

-- ==========================================
-- 4. 参与数据验证
-- ==========================================

-- 查看活动参与情况统计
SELECT 
    a.id AS '活动ID',
    a.title AS '活动标题',
    COUNT(ap.user_id) AS '当前参与人数',
    a.max_participants AS '最大参与人数',
    CASE 
        WHEN a.max_participants IS NULL THEN '无限制'
        WHEN COUNT(ap.user_id) >= a.max_participants THEN '已满'
        ELSE CONCAT('还可报名 ', (a.max_participants - COUNT(ap.user_id)), ' 人')
    END AS '报名状态',
    CASE 
        WHEN created_at_and_signup_start_time < NOW() 
         AND signup_end_time_and_activity_start_time > NOW() 
         AND a.status != 'finished' THEN 'open'
        WHEN signup_end_time_and_activity_start_time < NOW() 
         AND activity_end_time > NOW() 
         AND a.status != 'finished' THEN 'inprogress'
        WHEN activity_end_time < NOW() 
         OR a.status = 'finished' THEN 'finished'
        ELSE 'pending'
    END AS '当前状态'
FROM activities a
LEFT JOIN activity_participants ap ON a.id = ap.activity_id
WHERE a.status = 'published'
GROUP BY a.id, a.title, a.max_participants, a.status, 
         a.created_at_and_signup_start_time, 
         a.signup_end_time_and_activity_start_time, 
         a.activity_end_time
ORDER BY a.id;

-- 查看最近参与的用户（模拟头像接口逻辑）
SELECT 
    ap.activity_id AS '活动ID',
    a.title AS '活动标题',
    GROUP_CONCAT(
        u.nickname 
        ORDER BY ap.created_at DESC 
        SEPARATOR ', '
    ) AS '最近参与用户',
    COUNT(u.id) AS '参与人数总计'
FROM activity_participants ap
JOIN activities a ON ap.activity_id = a.id
LEFT JOIN users u ON ap.user_id = u.id
GROUP BY ap.activity_id, a.title
ORDER BY ap.activity_id;

-- ==========================================
-- 5. 索引验证
-- ==========================================

-- 查看活动表的索引
SELECT 
    INDEX_NAME AS '索引名',
    COLUMN_NAME AS '字段名',
    SEQ_IN_INDEX AS '序号',
    NON_UNIQUE AS '非唯一',
    INDEX_TYPE AS '索引类型'
FROM information_schema.STATISTICS 
WHERE TABLE_SCHEMA = 'wu_fei_city' 
  AND TABLE_NAME = 'activities'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

-- ==========================================
-- 6. 模拟API查询测试
-- ==========================================

-- 模拟开放报名活动查询（对应 /api/activities/open）
SELECT 
    a.id as activity_id,
    a.title,
    u.nickname as organizer_name,
    a.created_at_and_signup_start_time,
    a.signup_end_time_and_activity_start_time,
    a.activity_end_time,
    'open' as computed_status,
    COALESCE(a.image_url, 'https://cdn.example.com/activities/default.jpg') as image_url,
    a.location,
    COALESCE(COUNT(ap.user_id), 0) as participant_count,
    a.max_participants
FROM activities a
LEFT JOIN users u ON a.organizer_id = u.id
LEFT JOIN activity_participants ap ON a.id = ap.activity_id
WHERE a.created_at_and_signup_start_time < NOW() 
  AND a.signup_end_time_and_activity_start_time > NOW() 
  AND a.status != 'finished'
  AND a.status = 'published'
GROUP BY a.id, a.title, u.nickname, a.created_at_and_signup_start_time, 
         a.signup_end_time_and_activity_start_time, a.activity_end_time, 
         a.status, a.image_url, a.location, a.max_participants
ORDER BY a.created_at_and_signup_start_time DESC;

-- 模拟进行中活动查询（对应 /api/activities/inprogress）
SELECT 
    a.id as activity_id,
    a.title,
    u.nickname as organizer_name,
    a.created_at_and_signup_start_time,
    a.signup_end_time_and_activity_start_time,
    a.activity_end_time,
    'inprogress' as computed_status,
    COALESCE(a.image_url, 'https://cdn.example.com/activities/default.jpg') as image_url,
    a.location,
    COALESCE(COUNT(ap.user_id), 0) as participant_count,
    a.max_participants
FROM activities a
LEFT JOIN users u ON a.organizer_id = u.id
LEFT JOIN activity_participants ap ON a.id = ap.activity_id
WHERE a.signup_end_time_and_activity_start_time < NOW() 
  AND a.activity_end_time > NOW()
  AND a.status != 'finished'
  AND a.status = 'published'
GROUP BY a.id, a.title, u.nickname, a.created_at_and_signup_start_time, 
         a.signup_end_time_and_activity_start_time, a.activity_end_time, 
         a.status, a.image_url, a.location, a.max_participants
ORDER BY a.signup_end_time_and_activity_start_time DESC;

-- 模拟已结束活动查询（对应 /api/activities/finished）
SELECT 
    a.id as activity_id,
    a.title,
    u.nickname as organizer_name,
    a.created_at_and_signup_start_time,
    a.signup_end_time_and_activity_start_time,
    a.activity_end_time,
    'finished' as computed_status,
    COALESCE(a.image_url, 'https://cdn.example.com/activities/default.jpg') as image_url,
    a.location,
    COALESCE(COUNT(ap.user_id), 0) as participant_count,
    a.max_participants
FROM activities a
LEFT JOIN users u ON a.organizer_id = u.id
LEFT JOIN activity_participants ap ON a.id = ap.activity_id
WHERE a.activity_end_time < NOW() OR a.status = 'finished'
GROUP BY a.id, a.title, u.nickname, a.created_at_and_signup_start_time, 
         a.signup_end_time_and_activity_start_time, a.activity_end_time, 
         a.status, a.image_url, a.location, a.max_participants
ORDER BY a.updated_at DESC;

-- ==========================================
-- 测试完成提示
-- ==========================================

SELECT 
    '🎉 快速测试查询执行完成！' AS '状态',
    '请检查以上各项查询结果是否符合预期' AS '提示',
    '如有异常，请参考 TESTING_GUIDE.md 进行排查' AS '建议'; 