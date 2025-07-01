# 管理员审批接口文档

## 接口概述

管理员审批系统提供了对居委会用户注册申请进行审核的功能，支持单个审批和批量审批两种模式。

**基础信息：**
- **基础URL：** `http://localhost:8080`
- **认证方式：** JWT Bearer Token
- **Content-Type：** `application/json`
- **权限要求：** 管理员权限（userType: admin）

---

## 1. 单个用户审批

### 1.1 接口信息

- **接口地址：** `POST /api/admin/approve`
- **接口描述：** 对单个居委会用户进行审批（通过或拒绝）
- **权限要求：** 管理员权限

### 1.2 请求参数

#### Headers
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| Authorization | String | 是 | JWT令牌，格式：`Bearer {token}` |
| Content-Type | String | 是 | 固定值：`application/json` |

#### Body参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userId | Integer | 是 | 待审批用户的ID |
| approved | Boolean | 是 | 审批结果：true=通过，false=拒绝 |

### 1.3 请求示例

```bash
# cURL示例
curl -X POST "http://localhost:8080/api/admin/approve" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 5,
    "approved": true
  }'
```

```json
// 审批通过
{
    "userId": 5,
    "approved": true
}

// 审批拒绝
{
    "userId": 5,
    "approved": false
}
```

### 1.4 响应格式

#### 成功响应
```json
{
    "code": 200,
    "message": "审批通过成功",
    "data": null,
    "success": true
}
```

#### 失败响应
```json
{
    "code": 400,
    "message": "参数解析失败：userId不能为空",
    "data": null,
    "success": false
}
```

### 1.5 状态码说明

| HTTP状态码 | 响应码 | 说明 |
|------------|--------|------|
| 200 | 200 | 审批成功 |
| 200 | 400 | 请求参数错误 |
| 200 | 401 | JWT令牌无效或过期 |
| 200 | 403 | 权限不足（非管理员） |
| 200 | 500 | 审批失败（用户不存在或非居委会用户） |

---

## 2. 批量用户审批

### 2.1 接口信息

- **接口地址：** `POST /api/admin/batch-approve`
- **接口描述：** 对多个居委会用户进行批量审批
- **权限要求：** 管理员权限

### 2.2 请求参数

#### Headers
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| Authorization | String | 是 | JWT令牌，格式：`Bearer {token}` |
| Content-Type | String | 是 | 固定值：`application/json` |

#### Body参数
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| userIds | Array<Integer> | 是 | 待审批用户ID列表，不能为空 |
| approved | Boolean | 是 | 审批结果：true=全部通过，false=全部拒绝 |

### 2.3 请求示例

```bash
# cURL示例
curl -X POST "http://localhost:8080/api/admin/batch-approve" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "userIds": [1, 2, 3, 4, 5],
    "approved": true
  }'
```

```json
// 批量审批通过
{
    "userIds": [1, 2, 3, 4, 5],
    "approved": true
}

// 批量审批拒绝
{
    "userIds": [1, 2, 3, 4],
    "approved": false
}
```

### 2.4 响应格式

#### 成功响应
```json
{
    "code": 200,
    "message": "批量审批完成：4个成功，1个失败",
    "data": {
        "totalCount": 5,
        "successCount": 4,
        "failCount": 1,
        "failedUserIds": "3",
        "approved": true
    },
    "success": true
}
```

#### 失败响应
```json
{
    "code": 400,
    "message": "用户ID列表不能为空",
    "data": null,
    "success": false
}
```

### 2.5 响应数据字段说明

| 字段名 | 类型 | 描述 |
|--------|------|------|
| totalCount | Integer | 总处理用户数量 |
| successCount | Integer | 成功审批的用户数量 |
| failCount | Integer | 审批失败的用户数量 |
| failedUserIds | String | 审批失败的用户ID列表（逗号分隔） |
| approved | Boolean | 本次审批操作的结果（通过/拒绝） |

---

## 3. 获取待审批用户列表

### 3.1 接口信息

- **接口地址：** `GET /api/admin/pending-users`
- **接口描述：** 获取所有待审批的居委会用户列表
- **权限要求：** 管理员权限

### 3.2 请求参数

#### Headers
| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| Authorization | String | 是 | JWT令牌，格式：`Bearer {token}` |

### 3.3 请求示例

```bash
curl -X GET "http://localhost:8080/api/admin/pending-users" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### 3.4 响应示例

```json
{
    "code": 200,
    "message": "获取待审批用户列表成功",
    "data": [
        {
            "id": 1,
            "username": "committee_user1",
            "userType": "committee",
            "nickname": "某某居委会",
            "committeeDesc": "负责某某小区的环保事务",
            "contact": "联系电话：123-456-7890",
            "pending": 1,
            "approved": null,
            "createdAt": "2024-01-15T10:30:00"
        }
    ],
    "success": true
}
```

---

## 4. 错误码统一说明

| 错误码 | 描述 | 可能原因 |
|--------|------|----------|
| 400 | 请求参数错误 | JSON格式错误、必填参数缺失、参数类型不匹配 |
| 401 | 认证失败 | JWT令牌无效、过期或格式错误 |
| 403 | 权限不足 | 非管理员用户尝试访问管理员接口 |
| 500 | 服务器内部错误 | 数据库操作失败、业务逻辑异常 |

---

## 5. 完整的操作流程

### 5.1 准备工作

1. **管理员登录获取JWT令牌**
   ```json
   POST /api/admin/login
   {
       "username": "admin",
       "password": "admin123",
       "userType": "admin"
   }
   ```

2. **保存返回的JWT令牌**
   ```json
   {
       "code": 200,
       "data": {
           "token": "eyJhbGciOiJIUzI1NiJ9...",
           "username": "admin",
           "userType": "admin"
       }
   }
   ```

### 5.2 审批流程

1. **查看待审批用户列表**
   ```bash
   GET /api/admin/pending-users
   Headers: Authorization: Bearer {token}
   ```

2. **选择审批方式**
   - **单个审批：** 适用于需要逐个仔细审核的场景
   - **批量审批：** 适用于批量处理相同结果的场景

3. **执行审批操作**

### 5.3 常见使用场景

#### 场景1：逐个审核
```javascript
// 1. 获取待审批列表
const pendingUsers = await fetch('/api/admin/pending-users');

// 2. 对每个用户单独审批
for (const user of pendingUsers.data) {
    const approved = reviewUser(user); // 业务逻辑判断
    await fetch('/api/admin/approve', {
        method: 'POST',
        body: JSON.stringify({
            userId: user.id,
            approved: approved
        })
    });
}
```

#### 场景2：批量通过
```javascript
// 批量通过所有待审批用户
const userIds = [1, 2, 3, 4, 5];
await fetch('/api/admin/batch-approve', {
    method: 'POST',
    body: JSON.stringify({
        userIds: userIds,
        approved: true
    })
});
```

---

## 6. 测试用例

### 6.1 Postman测试集合

建议使用项目根目录下的 `postman-admin-approval-collection.json` 文件导入完整的测试用例。

### 6.2 手动测试步骤

1. **创建测试数据**（在MySQL中执行）：
   ```sql
   -- 创建待审批的居委会用户
   INSERT INTO users (username, password, user_type, nickname, committee_desc, contact, pending, approved) 
   VALUES 
   ('test_committee1', '$2a$10$encoded_password', 'committee', '测试居委会1', '测试描述1', '联系方式1', 1, NULL),
   ('test_committee2', '$2a$10$encoded_password', 'committee', '测试居委会2', '测试描述2', '联系方式2', 1, NULL);
   ```

2. **测试审批功能**
3. **验证结果**（在MySQL中查询）：
   ```sql
   SELECT id, username, user_type, pending, approved, updated_at 
   FROM users 
   WHERE user_type = 'committee';
   ```

---

## 7. 注意事项

### 7.1 安全考虑
- JWT令牌具有时效性，过期后需要重新登录
- 只有管理员用户可以执行审批操作
- 审批操作会记录操作时间（updated_at字段）

### 7.2 业务规则
- 只能审批 `user_type = 'committee'` 且 `pending = 1` 的用户
- 审批通过：`approved = 1`, `pending = 0`
- 审批拒绝：`approved = 0`, `pending = 0`
- 已审批的用户不能重复审批

### 7.3 错误处理
- 批量审批支持部分成功，会返回详细的成功/失败统计
- 单个审批失败会返回具体的错误原因
- 建议在前端添加适当的错误提示和重试机制

---

## 8. 更新日志

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| v1.0 | 2024-07-01 | 初始版本，包含单个审批和批量审批功能 |

---

*本文档基于 Wu_Fei_City 无废城市积分系统后端API v1.0* 