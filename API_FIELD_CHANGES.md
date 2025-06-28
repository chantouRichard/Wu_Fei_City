# 活动API字段变更说明文档

## 概述

本文档说明了"无废城市"项目中活动管理相关API的字段变更情况。此次变更主要是为了更好地表达活动时间逻辑，将时间字段重新设计为更符合业务需求的结构。

## 变更日期
- 更新时间：2025-01-27
- 影响版本：v2.0.0+

## 字段变更对比

### 旧字段结构（v1.x）
```json
{
  "startTime": "2025-07-01T08:00:00",     // 开始报名时间
  "endTime": "2025-07-05T18:00:00",      // 结束报名时间
  "createdAt": "2025-06-28T08:00:00"     // 活动创建时间
}
```

### 新字段结构（v2.0+）
```json
{
  "createdAtAndSignupStartTime": "2025-06-28T08:00:00",           // 活动创建时间 = 报名开始时间
  "signupEndTimeAndActivityStartTime": "2025-07-05T09:00:00",     // 报名结束时间 = 活动开始时间
  "activityEndTime": "2025-07-05T18:00:00"                       // 活动结束时间
}
```

## 业务逻辑说明

### 时间字段含义
1. **createdAtAndSignupStartTime**: 既是活动创建时间也是报名开始时间
   - 活动一旦创建，报名立即开始
   
2. **signupEndTimeAndActivityStartTime**: 既是报名结束时间也是活动开始时间
   - 报名截止时，活动立即开始进行
   
3. **activityEndTime**: 活动结束时间
   - 活动的实际结束时间

### 活动状态判断逻辑

#### 开放报名 (open)
```sql
created_at_and_signup_start_time < NOW() 
AND signup_end_time_and_activity_start_time > NOW() 
AND status != 'finished'
```
- 活动已创建且当前时间在报名截止前
- 用户可以报名参与

#### 进行中 (inprogress)
```sql
signup_end_time_and_activity_start_time < NOW() 
AND activity_end_time > NOW() 
AND status != 'finished'
```
- 报名已截止，活动正在进行
- 用户无法报名，但活动尚未结束

#### 已结束 (finished)
```sql
activity_end_time < NOW() 
OR status = 'finished'
```
- 活动已结束或被手动标记为结束
- 用户无法报名，活动已完成

## API接口响应格式

### GET /api/activities/open
获取开放报名的活动列表

```json
{
  "code": 200,
  "message": "获取开放报名活动列表成功",
  "data": [
    {
      "activityId": 1,
      "title": "社区清洁日",
      "organizerName": "东湖社区居委会",
      "createdAtAndSignupStartTime": "2025-06-28T08:00:00",
      "signupEndTimeAndActivityStartTime": "2025-07-05T09:00:00",
      "activityEndTime": "2025-07-05T18:00:00",
      "status": "open",
      "imageUrl": "https://cdn.example.com/activities/cleaning.jpg",
      "location": "东湖社区小广场",
      "avatars": [
        "https://cdn.example.com/users/avatar1.jpg",
        "https://cdn.example.com/users/avatar2.jpg",
        "https://cdn.example.com/users/avatar3.jpg"
      ],
      "participantCount": 26,
      "maxParticipants": 50
    }
  ]
}
```

### GET /api/activities/inprogress
获取进行中的活动列表

```json
{
  "code": 200,
  "message": "获取进行中活动列表成功",
  "data": [
    {
      "activityId": 2,
      "title": "绿色出行倡议",
      "organizerName": "西湖社区居委会",
      "createdAtAndSignupStartTime": "2024-06-10T08:00:00",
      "signupEndTimeAndActivityStartTime": "2024-06-20T09:00:00",
      "activityEndTime": "2024-07-20T20:00:00",
      "status": "inprogress",
      "imageUrl": "https://cdn.example.com/activities/green-travel.jpg",
      "location": "西湖社区文化广场",
      "avatars": [
        "https://cdn.example.com/users/avatar4.jpg",
        "https://cdn.example.com/users/avatar5.jpg"
      ],
      "participantCount": 18,
      "maxParticipants": 30
    }
  ]
}
```

### GET /api/activities/finished
获取已结束的活动列表

```json
{
  "code": 200,
  "message": "获取已结束活动列表成功",
  "data": [
    {
      "activityId": 3,
      "title": "垃圾分类知识竞赛",
      "organizerName": "南山社区居委会",
      "createdAtAndSignupStartTime": "2024-05-15T08:00:00",
      "signupEndTimeAndActivityStartTime": "2024-05-25T14:00:00",
      "activityEndTime": "2024-05-25T17:00:00",
      "status": "finished",
      "imageUrl": "https://cdn.example.com/activities/quiz.jpg",
      "location": "南山社区活动中心",
      "avatars": [
        "https://cdn.example.com/users/avatar6.jpg",
        "https://cdn.example.com/users/avatar7.jpg",
        "https://cdn.example.com/users/avatar8.jpg"
      ],
      "participantCount": 45,
      "maxParticipants": 50
    }
  ]
}
```

## 数据库迁移

### 表结构变更

#### 旧表结构
```sql
CREATE TABLE `activities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `organizer_id` INT NOT NULL,
  `start_time` DATETIME NOT NULL COMMENT '开始报名时间',
  `end_time` DATETIME NOT NULL COMMENT '结束报名时间',
  `location` VARCHAR(200) NOT NULL,
  `max_participants` INT DEFAULT NULL,
  `status` ENUM('pending','published','finished') DEFAULT 'pending',
  `image_url` TEXT DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);
```

#### 新表结构
```sql
CREATE TABLE `activities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `organizer_id` INT NOT NULL,
  `signup_end_time_and_activity_start_time` DATETIME NOT NULL COMMENT '报名结束时间/活动开始时间',
  `activity_end_time` DATETIME NOT NULL COMMENT '活动结束时间',
  `location` VARCHAR(200) NOT NULL,
  `max_participants` INT DEFAULT NULL,
  `status` ENUM('pending','published','finished') DEFAULT 'pending',
  `image_url` TEXT DEFAULT NULL,
  `created_at_and_signup_start_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '活动创建时间/报名开始时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);
```

### 迁移步骤
1. 备份现有数据
2. 添加新字段
3. 数据迁移（将旧字段值复制到新字段）
4. 设置新字段约束
5. 删除旧字段
6. 重建索引

详细迁移脚本请参考 `src/main/resources/sql/cleanup.sql`

## 兼容性说明

### 向后兼容性
- **不兼容**：此次变更不向后兼容
- 前端应用需要同步更新以使用新的字段名称
- 建议在测试环境充分测试后再部署到生产环境

### 升级建议
1. 在测试环境验证新字段逻辑
2. 更新前端代码以适配新的API响应格式
3. 执行数据库迁移脚本
4. 部署新版本后端代码
5. 验证所有功能正常工作

## 影响的组件

### 后端组件
- `Activity.java` - 实体类
- `ActivityListResponse.java` - DTO响应类
- `ActivityMapper.xml` - MyBatis映射文件
- `ActivityService.java` - 业务逻辑层
- `ActivityController.java` - 控制器层

### 数据库
- `activities` 表结构
- 相关索引
- 测试数据

### 前端（需要相应更新）
- 活动列表页面
- 活动详情页面
- 活动管理页面
- 时间显示组件

## 注意事项

1. **时间逻辑**：新的时间逻辑更符合实际业务需求，活动创建即开始报名，报名截止即活动开始
2. **数据完整性**：迁移过程中需要确保数据完整性，建议在低峰期执行
3. **测试验证**：充分测试各种时间场景下的活动状态判断逻辑
4. **监控告警**：部署后需要密切关注相关API的响应和错误日志

## 联系信息

如有疑问，请联系开发团队：
- 邮箱：wufei@whu.edu.cn
- 项目地址：[GitHub仓库地址] 