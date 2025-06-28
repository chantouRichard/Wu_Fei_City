-- ======================================
-- 数据库初始化脚本 - 无废城市积分系统
-- ======================================

-- ==========================================
-- 第一步：清理旧数据（防止干扰）
-- ==========================================
-- 注意：以下清理步骤会删除所有现有数据，请谨慎使用！

-- 1. 删除可能存在的视图（必须先删除视图，因为它依赖于表）
DROP VIEW IF EXISTS user_score_ranking;

-- 2. 删除可能存在的表（按依赖关系倒序删除）
-- 先删除有外键依赖的表
DROP TABLE IF EXISTS activity_participants;
DROP TABLE IF EXISTS activities;
DROP TABLE IF EXISTS green_score_records;

-- 再删除主表
DROP TABLE IF EXISTS users;

-- 3. 删除数据库（如果需要完全重建）
-- DROP DATABASE IF EXISTS wu_fei_city;

-- ==========================================
-- 第二步：创建数据库和表结构
-- ==========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS wu_fei_city DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE wu_fei_city;

-- 创建用户表
CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL COMMENT '登录用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '登录密码(BCrypt加密)',
  `user_type` ENUM('normal','committee','admin') NOT NULL DEFAULT 'normal' COMMENT '用户类型',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '用户昵称',
  `introduction` TEXT DEFAULT NULL COMMENT '个人介绍，仅normal用户使用',
  `avatar` TEXT DEFAULT NULL COMMENT '头像URL',
  `committee_desc` VARCHAR(200) DEFAULT NULL COMMENT '居委会描述',
  `contact` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `pending` TINYINT(1) DEFAULT 0 COMMENT '是否待审批(0否1是)',
  `approved` TINYINT(1) DEFAULT NULL COMMENT '审批状态(NULL未处理/1通过/0拒绝)',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`),
  KEY `idx_user_type` (`user_type`),
  KEY `idx_pending` (`pending`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 创建绿色积分记录表
CREATE TABLE IF NOT EXISTS `green_score_records` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL COMMENT '用户ID',
  `score` INT NOT NULL COMMENT '积分值(可正可负)',
  `action_type` VARCHAR(50) NOT NULL COMMENT '行为类型(如:recycling,energy_saving,activity_participation等)',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '行为描述',
  `record_date` DATE NOT NULL COMMENT '记录日期',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_record_date` (`record_date`),
  KEY `idx_action_type` (`action_type`),
  CONSTRAINT `fk_green_score_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='绿色积分记录表';

-- 创建社区活动表（使用新字段结构）
CREATE TABLE IF NOT EXISTS `activities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL COMMENT '活动标题',
  `description` TEXT COMMENT '活动详情',
  `organizer_id` INT NOT NULL COMMENT '组织者(居委会用户ID)',
  `signup_end_time_and_activity_start_time` DATETIME NOT NULL COMMENT '报名结束时间/活动开始时间',
  `activity_end_time` DATETIME NOT NULL COMMENT '活动结束时间',
  `location` VARCHAR(200) NOT NULL COMMENT '活动地点',
  `max_participants` INT DEFAULT NULL COMMENT '最大参与人数',
  `status` ENUM('pending','published','finished') DEFAULT 'pending' COMMENT '活动状态',
  `image_url` TEXT DEFAULT NULL COMMENT '活动图片URL',
  `created_at_and_signup_start_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '活动创建时间/报名开始时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_organizer` (`organizer_id`),
  KEY `idx_time` (`created_at_and_signup_start_time`,`signup_end_time_and_activity_start_time`,`activity_end_time`),
  KEY `idx_status` (`status`),
  -- 时间逻辑约束：创建时间 < 活动开始时间 < 活动结束时间
  CONSTRAINT `chk_time_sequence` CHECK (`created_at_and_signup_start_time` < `signup_end_time_and_activity_start_time` AND `signup_end_time_and_activity_start_time` < `activity_end_time`),
  CONSTRAINT `fk_activity_organizer` FOREIGN KEY (`organizer_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区活动表';

-- 创建活动参与记录表
CREATE TABLE IF NOT EXISTS `activity_participants` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `activity_id` INT NOT NULL COMMENT '关联活动ID',
  `user_id` INT NOT NULL COMMENT '参与用户ID',
  `attended` TINYINT(1) DEFAULT 0 COMMENT '是否实际参与(居委会确认)',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_activity_user` (`activity_id`,`user_id`) COMMENT '防止重复参与',
  KEY `idx_user` (`user_id`),
  CONSTRAINT `fk_participant_activity` FOREIGN KEY (`activity_id`) REFERENCES `activities` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_participant_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动参与记录表';

-- 创建用户分数排名视图
CREATE VIEW user_score_ranking AS
SELECT 
    u.id,
    u.nickname,
    u.avatar,
    COALESCE(SUM(g.score), 0) AS total_score,
    RANK() OVER (ORDER BY COALESCE(SUM(g.score), 0) DESC) AS `rank`
FROM users u
LEFT JOIN green_score_records g ON u.id = g.user_id
WHERE u.user_type = 'normal'
GROUP BY u.id, u.nickname, u.avatar;

-- ==========================================
-- 第三步：插入初始数据
-- ==========================================

-- 插入默认管理员账号
-- 密码是 admin123 经过BCrypt加密后的结果
INSERT INTO `users` (`username`, `password`, `user_type`, `nickname`, `pending`, `approved`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'admin', '系统管理员', 0, 1); 

-- 插入一些测试普通用户
INSERT INTO `users` (`username`, `password`, `user_type`, `nickname`, `pending`, `approved`) VALUES
('testuser1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'normal', '测试用户1', 0, 1),
('testuser2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'normal', '测试用户2', 0, 1),
('testuser3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'normal', '测试用户3', 0, 1);

-- 插入测试居委会用户（用于组织活动）
INSERT INTO `users` (`username`, `password`, `user_type`, `nickname`, `committee_desc`, `contact`, `pending`, `approved`) VALUES
('committee1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'committee', '测试居委会1', '负责社区环保活动组织', '13800138001', 0, 1),
('committee2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'committee', '测试居委会2', '负责社区绿化管理', '13800138002', 0, 1);

-- 插入一些测试积分数据（使用刚创建的测试用户）
INSERT INTO `green_score_records` (`user_id`, `score`, `action_type`, `description`, `record_date`) VALUES
-- 为测试用户1添加积分记录
((SELECT id FROM users WHERE username = 'testuser1'), 10, 'recycling', '垃圾分类回收', '2024-01-01'),
((SELECT id FROM users WHERE username = 'testuser1'), 15, 'energy_saving', '节能减排行为', '2024-01-02'),
((SELECT id FROM users WHERE username = 'testuser1'), 20, 'activity_participation', '参与环保活动', '2024-01-03'),
-- 为测试用户2添加积分记录
((SELECT id FROM users WHERE username = 'testuser2'), 12, 'recycling', '垃圾分类回收', '2024-01-01'),
((SELECT id FROM users WHERE username = 'testuser2'), 18, 'green_commute', '绿色出行', '2024-01-02'),
-- 为测试用户3添加积分记录
((SELECT id FROM users WHERE username = 'testuser3'), 8, 'water_saving', '节约用水', '2024-01-01'),
((SELECT id FROM users WHERE username = 'testuser3'), 25, 'volunteer_activity', '志愿活动', '2024-01-02');

-- 插入测试活动数据（使用新字段结构）
-- 时间逻辑：created_at_and_signup_start_time < signup_end_time_and_activity_start_time < activity_end_time
INSERT INTO `activities` (`title`, `description`, `organizer_id`, `signup_end_time_and_activity_start_time`, `activity_end_time`, `location`, `max_participants`, `status`, `image_url`, `created_at_and_signup_start_time`) VALUES
-- 由居委会1组织的活动
('社区垃圾分类宣传活动', '提高居民垃圾分类意识，学习正确的垃圾分类方法', (SELECT id FROM users WHERE username = 'committee1'), '2025-12-15 18:00:00', '2025-12-15 21:00:00', '社区广场', 50, 'published', 'https://cdn.example.com/activities/garbage-sorting.jpg', '2024-12-01 09:00:00'),
('绿色出行倡议活动', '倡导居民使用公共交通、自行车等绿色出行方式', (SELECT id FROM users WHERE username = 'committee1'), '2025-12-20 20:00:00', '2025-12-20 23:00:00', '社区门口', 30, 'published', 'https://cdn.example.com/activities/green-travel.jpg', '2024-12-10 08:00:00'),
('节能减排知识讲座', '邀请专家讲解家庭节能减排小技巧', (SELECT id FROM users WHERE username = 'committee1'), '2025-01-10 16:00:00', '2025-10-10 19:00:00', '社区活动中心', 100, 'published', 'https://cdn.example.com/activities/energy-saving.jpg', '2025-01-01 14:00:00'),
-- 由居委会2组织的活动  
('社区植树活动', '在社区空地种植绿色植物，美化环境', (SELECT id FROM users WHERE username = 'committee2'), '2024-11-12 19:00:00', '2024-11-12 22:00:00', '社区花园', 25, 'finished', 'https://cdn.example.com/activities/tree-planting.jpg', '2024-11-05 07:00:00'),
('环保手工制作活动', '利用废旧物品制作实用的环保手工艺品', (SELECT id FROM users WHERE username = 'committee2'), '2024-11-25 17:00:00', '2024-11-25 20:00:00', '社区活动室', 20, 'finished', 'https://cdn.example.com/activities/handicraft.jpg', '2024-11-20 10:00:00');

-- 插入活动参与记录
INSERT INTO `activity_participants` (`activity_id`, `user_id`, `attended`) VALUES
-- 垃圾分类宣传活动的参与者
((SELECT id FROM activities WHERE title = '社区垃圾分类宣传活动'), (SELECT id FROM users WHERE username = 'testuser1'), 1),
((SELECT id FROM activities WHERE title = '社区垃圾分类宣传活动'), (SELECT id FROM users WHERE username = 'testuser2'), 1),
((SELECT id FROM activities WHERE title = '社区垃圾分类宣传活动'), (SELECT id FROM users WHERE username = 'testuser3'), 0),
-- 绿色出行倡议活动的参与者
((SELECT id FROM activities WHERE title = '绿色出行倡议活动'), (SELECT id FROM users WHERE username = 'testuser1'), 1),
((SELECT id FROM activities WHERE title = '绿色出行倡议活动'), (SELECT id FROM users WHERE username = 'testuser3'), 1),
-- 社区植树活动的参与者
((SELECT id FROM activities WHERE title = '社区植树活动'), (SELECT id FROM users WHERE username = 'testuser1'), 1),
((SELECT id FROM activities WHERE title = '社区植树活动'), (SELECT id FROM users WHERE username = 'testuser2'), 1),
-- 环保手工制作活动的参与者
((SELECT id FROM activities WHERE title = '环保手工制作活动'), (SELECT id FROM users WHERE username = 'testuser2'), 1),
((SELECT id FROM activities WHERE title = '环保手工制作活动'), (SELECT id FROM users WHERE username = 'testuser3'), 1);

-- ==========================================
-- 初始化完成提示
-- ==========================================
-- 以下查询用于验证初始化结果（可选执行）

-- SELECT '数据库初始化完成!' AS status;
-- SELECT COUNT(*) AS user_count FROM users;
-- SELECT COUNT(*) AS score_record_count FROM green_score_records;
-- SELECT COUNT(*) AS activity_count FROM activities;
-- SELECT COUNT(*) AS participant_count FROM activity_participants;
-- SELECT * FROM user_score_ranking ORDER BY `rank` LIMIT 5;

-- 验证活动数据
-- SELECT a.title, u.nickname AS organizer, a.status, a.max_participants 
-- FROM activities a 
-- JOIN users u ON a.organizer_id = u.id 
-- ORDER BY a.created_at;

-- 验证参与记录
-- SELECT a.title AS activity, u.nickname AS participant, ap.attended 
-- FROM activity_participants ap
-- JOIN activities a ON ap.activity_id = a.id
-- JOIN users u ON ap.user_id = u.id
-- ORDER BY a.title, u.nickname; 



SELECT '数据库初始化完成!' AS status; 