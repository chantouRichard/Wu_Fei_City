# 时间模拟功能使用指南

## 📅 **功能概述**

为了方便测试创建活动接口，我们实现了时间模拟功能。在测试模式下，您可以模拟任意的"当前时间"，从而测试不同时间场景下的活动创建逻辑。

## ⚙️ **配置说明**

### application.properties 配置
```properties
# 测试模式配置
app.test-mode.enabled=true                              # 启用测试模式
app.test-mode.mock-current-time=2024-12-01T10:00:00    # 默认模拟时间
```

### 配置参数
- `app.test-mode.enabled`: 是否启用测试模式（true/false）
- `app.test-mode.mock-current-time`: 默认的模拟时间（ISO 8601格式）

## 🚀 **使用方法**

### 方法1：配置文件设置（静态）

修改 `application.properties` 中的模拟时间：
```properties
app.test-mode.mock-current-time=2024-12-01T10:00:00
```

重启应用后生效。

### 方法2：API动态设置（推荐）

使用测试接口动态调整时间，无需重启应用。

#### 1. 查看当前时间信息
```bash
GET http://localhost:8080/api/test/time
```

**响应示例**：
```json
{
  "message": "获取时间信息成功",
  "data": "测试模式: true, 模拟时间: 2024-12-01T10:00:00, 当前返回时间: 2024-12-01T10:00:00",
  "code": 200
}
```

#### 2. 设置自定义模拟时间
```bash
POST http://localhost:8080/api/test/time/mock?mockTime=2024-12-01T10:00:00
```

#### 3. 使用预设时间
```bash
POST http://localhost:8080/api/test/time/preset?preset=current
```

**预设时间选项**：
- `past`: 2024-01-01T10:00:00 （过去时间）
- `current`: 2024-12-01T10:00:00 （当前时间）
- `future`: 2026-01-01T10:00:00 （未来时间）

#### 4. 重置为真实时间
```bash
DELETE http://localhost:8080/api/test/time/mock
```

## 🧪 **测试场景**

### 场景1：测试成功创建活动

1. **设置当前时间为过去时间**：
```bash
POST http://localhost:8080/api/test/time/mock?mockTime=2024-12-01T10:00:00
```

2. **创建活动（活动开始时间在未来）**：
```json
{
  "title": "社区清洁日",
  "signup_end_time_and_activity_start_time": "2025-02-15T09:00:00",
  "activity_end_time": "2025-02-15T12:00:00",
  "location": "社区公园"
}
```

✅ **预期结果**：创建成功（2024-12-01 < 2025-02-15 < 2025-02-15）

### 场景2：测试时间验证失败

1. **设置当前时间为未来时间**：
```bash
POST http://localhost:8080/api/test/time/mock?mockTime=2026-01-01T10:00:00
```

2. **创建活动（活动开始时间在过去）**：
```json
{
  "title": "社区清洁日",
  "signup_end_time_and_activity_start_time": "2025-02-15T09:00:00",
  "activity_end_time": "2025-02-15T12:00:00",
  "location": "社区公园"
}
```

❌ **预期结果**：时间验证失败（2026-01-01 > 2025-02-15，违反时间逻辑）

### 场景3：测试边界时间

1. **设置当前时间接近活动开始时间**：
```bash
POST http://localhost:8080/api/test/time/mock?mockTime=2025-02-15T08:59:00
```

2. **创建活动（1分钟后开始）**：
```json
{
  "title": "社区清洁日",
  "signup_end_time_and_activity_start_time": "2025-02-15T09:00:00",
  "activity_end_time": "2025-02-15T12:00:00",
  "location": "社区公园"
}
```

✅ **预期结果**：创建成功（边界时间测试）

## 📋 **完整测试流程**

### 1. 启动应用并检查测试模式
```bash
# 启动应用
./mvnw spring-boot:run

# 检查时间服务状态
curl http://localhost:8080/api/test/time
```

### 2. 设置合适的测试时间
```bash
# 设置为2024年12月1日（便于测试2025年的活动）
curl -X POST "http://localhost:8080/api/test/time/mock?mockTime=2024-12-01T10:00:00"
```

### 3. 测试创建活动
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

### 4. 验证活动状态
```bash
# 查看开放报名的活动
curl http://localhost:8080/api/activities/open
```

## 🔧 **Postman 测试集合更新**

在Postman中，您可以添加一个"前置脚本"来自动设置测试时间：

```javascript
// 在创建活动测试之前，先设置模拟时间
pm.sendRequest({
    url: pm.environment.get("baseUrl") + "/api/test/time/mock",
    method: "POST",
    header: {
        "Content-Type": "application/x-www-form-urlencoded"
    },
    body: {
        mode: "urlencoded",
        urlencoded: [
            {key: "mockTime", value: "2024-12-01T10:00:00"}
        ]
    }
}, function (err, res) {
    if (err) {
        console.log("设置模拟时间失败:", err);
    } else {
        console.log("模拟时间设置成功:", res.json());
    }
});
```

## 🎯 **注意事项**

1. **测试模式安全**：
   - 时间模拟功能仅在 `app.test-mode.enabled=true` 时生效
   - 生产环境应设置为 `false`

2. **时间格式**：
   - 必须使用 ISO 8601 格式：`yyyy-MM-dd'T'HH:mm:ss`
   - 例如：`2024-12-01T10:00:00`

3. **时间逻辑**：
   - 时间验证逻辑：`当前时间 < 活动开始时间 < 活动结束时间`
   - 如果模拟时间晚于活动开始时间，创建会失败

4. **重启影响**：
   - 通过API设置的模拟时间在应用重启后会重置为配置文件中的值
   - 配置文件中的模拟时间在重启后保持

## 📊 **日志监控**

启用DEBUG日志后，可以看到详细的时间处理信息：

```
DEBUG - 使用模拟时间: 2024-12-01T10:00:00
DEBUG - 使用时间服务获取当前时间: 2024-12-01T10:00:00, 时间服务信息: 测试模式: true, 模拟时间: 2024-12-01T10:00:00, 当前返回时间: 2024-12-01T10:00:00
```

## 🚀 **快速开始**

```bash
# 1. 启动应用（已配置测试模式）
./mvnw spring-boot:run

# 2. 设置测试时间
curl -X POST "http://localhost:8080/api/test/time/preset?preset=current"

# 3. 测试创建活动
curl -X POST http://localhost:8080/api/activities/create \
  -H "Content-Type: application/json" \
  -d '{
    "title": "测试活动",
    "signup_end_time_and_activity_start_time": "2025-01-15T09:00:00",
    "activity_end_time": "2025-01-15T12:00:00",
    "location": "测试地点"
  }'

# 4. 验证结果
curl http://localhost:8080/api/activities/open
```

现在您可以轻松模拟任意时间点来测试活动创建功能了！🎉 