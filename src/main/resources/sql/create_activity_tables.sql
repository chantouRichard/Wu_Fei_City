-- ==========================================
-- 活动管理表创建脚本
-- 创建时间: 2024-06-27
-- 说明: 用于创建社区活动管理相关的数据表
-- ==========================================

USE wu_fei_city;

-- 创建社区活动表
CREATE TABLE IF NOT EXISTS `activities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL COMMENT '活动标题',
  `description` TEXT COMMENT '活动详情',
  `organizer_id` INT NOT NULL COMMENT '组织者(居委会用户ID)',
  `start_time` DATETIME NOT NULL COMMENT '开始报名时间',
  `end_time` DATETIME NOT NULL COMMENT '结束报名时间',
  `location` VARCHAR(200) NOT NULL COMMENT '活动地点',
  `max_participants` INT DEFAULT NULL COMMENT '最大参与人数',
  `status` ENUM('pending','published','finished') DEFAULT 'pending' COMMENT '活动状态',
  `image_url` TEXT DEFAULT NULL COMMENT '活动图片URL',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_organizer` (`organizer_id`),
  KEY `idx_time` (`start_time`,`end_time`),
  KEY `idx_status` (`status`),
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
  KEY `idx_attended` (`attended`),
  CONSTRAINT `fk_participant_activity` FOREIGN KEY (`activity_id`) REFERENCES `activities` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_participant_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动参与记录表';

-- 验证表创建结果
SELECT '活动表创建完成!' AS status;
SHOW TABLES LIKE '%activit%'; 