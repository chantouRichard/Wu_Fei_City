# 创建活动接口 API 测试指南

## 接口概述

### 接口路径
```
POST /api/activities/create
```

### 功能描述
居委会用户创建新的社区活动接口。活动创建成功后会自动设置状态为"published"，创建时间作为报名开始时间。

## 请求参数

### Headers
```
Content-Type: application/json
```

### 请求体字段

| 字段名 | 类型 | 必填 | 验证规则 | 描述 |
|--------|------|------|----------|------|
| `title` | String | 是 | 长度 >= 2 | 活动标题 |
| `description` | String | 否 | - | 活动详情描述 |
| `signup_end_time_and_activity_start_time` | String | 是 | ISO 8601格式 | 报名结束时间/活动开始时间 |
| `activity_end_time` | String | 是 | ISO 8601格式 | 活动结束时间 |
| `location` | String | 是 | 非空 | 活动地点 |
| `max_participants` | Integer | 否 | - | 最大参与人数 |
| `image_url` | String | 否 | - | 活动图片URL |

### 时间逻辑验证
```
当前时间 < signup_end_time_and_activity_start_time < activity_end_time
```

### 请求体示例
```json
{
  "title": "社区清洁日",
  "description": "组织居民清理社区卫生，美化生活环境",
  "signup_end_time_and_activity_start_time": "2025-02-15T09:00:00",
  "activity_end_time": "2025-02-15T12:00:00",
  "location": "社区公园",
  "max_participants": 50,
  "image_url": "https://cdn.example.com/activities/cleaning.jpg"
}
```

## 响应格式

### 成功响应 (200)
```json
{
  "message": "活动创建成功",
  "data": {
    "activity_id": 1,
    "title": "社区清洁日",
    "description": "组织居民清理社区卫生，美化生活环境",
    "created_at_and_signup_start_time": "2025-01-27T10:30:00",
    "signup_end_time_and_activity_start_time": "2025-02-15T09:00:00",
    "activity_end_time": "2025-02-15T12:00:00",
    "location": "社区公园",
    "max_participants": 50,
    "status": "published",
    "image_url": "https://cdn.example.com/activities/cleaning.jpg",
    "organizer_id": 1
  },
  "code": 200
}
```

### 错误响应

#### 参数验证失败 (400)
```json
{
  "message": "活动标题长度至少为2个字符",
  "code": 400
}
```

```json
{
  "message": "活动地点不能为空",
  "code": 400
}
```

```json
{
  "message": "时间设置不正确：活动开始时间必须晚于当前时间，活动结束时间必须晚于活动开始时间",
  "code": 400
}
```

#### 服务器内部错误 (500)
```json
{
  "message": "服务器内部错误: 数据库连接失败",
  "code": 500
}
```

## 字段说明

### 时间字段详解
- **`created_at_and_signup_start_time`**: 活动创建时间，同时也是报名开始时间，由服务器自动设置为当前时间
- **`signup_end_time_and_activity_start_time`**: 报名结束时间，同时也是活动开始时间
- **`activity_end_time`**: 活动结束时间

### 业务逻辑
1. 居委会发起创建活动请求
2. 系统验证请求参数
3. 设置创建时间为当前时间（作为报名开始时间）
4. 设置活动状态为"published"
5. 分配组织者ID（从用户身份获取）
6. 保存到数据库并返回完整活动信息

## 测试用例

### 1. 成功创建活动
```bash
curl -X POST http://localhost:8080/api/activities/create \
  -H "Content-Type: application/json" \
  -d '{
    "title": "社区清洁日",
    "description": "组织居民清理社区卫生，美化生活环境",
    "signup_end_time_and_activity_start_time": "2025-02-15T09:00:00",
    "activity_end_time": "2025-02-15T12:00:00",
    "location": "社区公园",
    "max_participants": 50,
    "image_url": "https://cdn.example.com/activities/cleaning.jpg"
  }'
```

### 2. 标题长度不足
```bash
curl -X POST http://localhost:8080/api/activities/create \
  -H "Content-Type: application/json" \
  -d '{
    "title": "短",
    "signup_end_time_and_activity_start_time": "2025-02-15T09:00:00",
    "activity_end_time": "2025-02-15T12:00:00",
    "location": "社区公园"
  }'
```

### 3. 地点为空
```bash
curl -X POST http://localhost:8080/api/activities/create \
  -H "Content-Type: application/json" \
  -d '{
    "title": "社区清洁日",
    "signup_end_time_and_activity_start_time": "2025-02-15T09:00:00",
    "activity_end_time": "2025-02-15T12:00:00",
    "location": ""
  }'
```

### 4. 时间逻辑错误
```bash
curl -X POST http://localhost:8080/api/activities/create \
  -H "Content-Type: application/json" \
  -d '{
    "title": "社区清洁日",
    "signup_end_time_and_activity_start_time": "2023-01-01T09:00:00",
    "activity_end_time": "2023-01-01T08:00:00",
    "location": "社区公园"
  }'
```

## Postman 测试

### 导入测试集合
1. 导入 `postman-create-activity-test.json` 文件
2. 设置环境变量 `baseUrl` 为 `http://localhost:8080`
3. 运行整个测试集合

### 测试项目
- **成功案例测试**: 验证正常创建活动的完整流程
- **参数验证测试**: 验证各种参数验证规则
- **时间逻辑测试**: 验证时间顺序验证
- **完整字段测试**: 验证所有可选字段的处理
- **查询验证测试**: 验证创建的活动能在开放活动列表中查询到

## 数据库验证

### 查询新创建的活动
```sql
-- 查看所有活动
SELECT * FROM activities ORDER BY created_at_and_signup_start_time DESC LIMIT 5;

-- 查看活动时间字段
SELECT 
    id,
    title,
    created_at_and_signup_start_time,
    signup_end_time_and_activity_start_time,
    activity_end_time,
    status
FROM activities 
WHERE title = '社区清洁日'
ORDER BY created_at_and_signup_start_time DESC;

-- 验证时间逻辑约束
SELECT 
    id,
    title,
    CASE 
        WHEN created_at_and_signup_start_time < signup_end_time_and_activity_start_time 
         AND signup_end_time_and_activity_start_time < activity_end_time 
        THEN '时间逻辑正确'
        ELSE '时间逻辑错误'
    END as time_check
FROM activities
WHERE id = [新创建的活动ID];
```

### 验证活动状态
```sql
-- 验证开放报名的活动
SELECT 
    id,
    title,
    status,
    CASE 
        WHEN created_at_and_signup_start_time < NOW() 
         AND signup_end_time_and_activity_start_time > NOW() 
         AND status != 'finished' 
        THEN 'open'
        ELSE '其他状态'
    END as computed_status
FROM activities 
WHERE title = '社区清洁日';
```

## 注意事项

1. **时间格式**: 所有时间字段必须使用 ISO 8601 格式 (`YYYY-MM-DDTHH:mm:ss`)
2. **时区处理**: 当前实现使用服务器本地时间，生产环境需要考虑时区处理
3. **权限验证**: 当前版本使用硬编码的组织者ID，实际应用需要从JWT token获取用户身份
4. **数据持久化**: 创建的活动会立即保存到数据库
5. **状态管理**: 新创建的活动状态自动设置为"published"
6. **验证规则**: 客户端和服务端都需要进行参数验证

## 错误排查

### 常见问题

1. **时间验证失败**
   - 检查时间格式是否正确
   - 确认时间逻辑：当前时间 < 活动开始时间 < 活动结束时间

2. **数据库连接失败**
   - 检查数据库服务是否启动
   - 验证数据库连接配置

3. **参数格式错误**
   - 检查JSON格式是否正确
   - 验证Content-Type header

4. **编译错误**
   - 运行 `./mvnw compile -q` 检查编译状态
   - 检查依赖项是否正确

### 日志查看
```bash
# 查看应用日志
tail -f logs/spring.log

# 或在IDE中查看控制台输出
```

## 开发进度

✅ 已完成：
- 创建活动请求DTO (`CreateActivityRequest`)
- 创建活动响应DTO (`CreateActivityResponse`)
- 数据库映射更新 (ActivityMapper.xml)
- 业务逻辑实现 (ActivityService)
- 控制器接口实现 (ActivityController)
- 参数验证和错误处理
- Postman测试集合
- 完整的测试指南文档

🔄 待完善：
- JWT token集成获取真实用户ID
- 更完善的权限验证
- 时区处理
- 文件上传功能（活动图片）
- 活动审核流程