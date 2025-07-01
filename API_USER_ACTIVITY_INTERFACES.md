# 用户活动管理接口文档

## 📋 接口概述

本文档描述了**用户活动管理模块**的三个核心接口，用于普通用户查询活动参与情况。系统根据用户与活动的关系，将活动分为三种状态：

- **已报名活动**（joined）：用户已报名但尚未参与完成的活动
- **可报名活动**（available）：用户当前可以报名参与的活动
- **历史参与活动**（history）：用户已实际参与完成的活动

---

## 🔐 认证说明

所有接口都需要用户身份验证，请在请求头中携带有效的JWT Token：

```http
Authorization: Bearer your_jwt_token_here
```

---

## 📍 接口列表

### 1. 获取用户已报名的活动列表

**接口地址**：`GET /api/user/{userId}/activities/joined`

**接口描述**：获取用户已报名但尚未参与完成的活动列表，这些活动用户已经成功报名，等待参与。

#### 请求参数

| 参数名 | 类型 | 位置 | 必填 | 说明 |
|--------|------|------|------|------|
| userId | Integer | Path | 是 | 用户ID |

#### 业务逻辑

- `attended = 0`（表示报名但未参与完成）
- `activity.status ≠ 'finished'`（活动未结束）
- `activity.status = 'published'`（活动已发布）

#### 响应示例

```json
{
  "success": true,
  "message": "获取已报名活动列表成功",
  "data": [
    {
      "activityId": 101,
      "title": "社区垃圾分类宣传活动",
      "organizerName": "东湖社区居委会",
      "signupEndTimeAndActivityStartTime": "2025-07-04T18:00:00",
      "activityEndTime": "2025-07-04T21:00:00",
      "status": "joined",
      "imageUrl": "https://cdn.example.com/activities/garbage-sorting.jpg",
      "location": "社区广场",
      "participantCount": 16,
      "maxParticipants": 50,
      "avatars": [
        "https://example.com/avatar1.jpg",
        "https://example.com/avatar2.jpg",
        "https://example.com/avatar3.jpg"
      ]
    }
  ]
}
```

#### 错误响应

```json
{
  "success": false,
  "message": "获取已报名活动列表失败: 用户不存在",
  "data": null
}
```

---

### 2. 获取用户可报名的活动列表

**接口地址**：`GET /api/user/{userId}/activities/available`

**接口描述**：获取用户当前可以报名参与的活动列表，这些活动还在报名期内且未满员。

#### 请求参数

| 参数名 | 类型 | 位置 | 必填 | 说明 |
|--------|------|------|------|------|
| userId | Integer | Path | 是 | 用户ID |

#### 业务逻辑

- `signup_end_time_and_activity_start_time > NOW()`（还可以报名）
- `activity.status = 'published'`（活动已发布）
- 活动未满员（`participant_count < max_participants` 或 `max_participants` 为NULL）
- 用户未报名该活动

#### 响应示例

```json
{
  "success": true,
  "message": "获取可报名活动列表成功",
  "data": [
    {
      "activityId": 102,
      "title": "绿色出行倡议活动",
      "organizerName": "东湖社区居委会",
      "signupEndTimeAndActivityStartTime": "2025-08-15T19:00:00",
      "activityEndTime": "2025-08-15T22:00:00",
      "status": "available",
      "imageUrl": "https://cdn.example.com/activities/green-travel.jpg",
      "location": "社区门口",
      "participantCount": 8,
      "maxParticipants": 30,
      "avatars": [
        "https://example.com/avatar4.jpg",
        "https://example.com/avatar5.jpg"
      ]
    }
  ]
}
```

#### 错误响应

```json
{
  "success": false,
  "message": "获取可报名活动列表失败: 数据库连接异常",
  "data": null
}
```

---

### 3. 获取用户历史参与的活动列表

**接口地址**：`GET /api/user/{userId}/activities/history`

**接口描述**：获取用户已实际参与完成的活动列表，这些活动用户已经完成参与并由居委会确认。

#### 请求参数

| 参数名 | 类型 | 位置 | 必填 | 说明 |
|--------|------|------|------|------|
| userId | Integer | Path | 是 | 用户ID |

#### 业务逻辑

- `attended = 1`（表示已实际参与完成）

#### 响应示例

```json
{
  "success": true,
  "message": "获取历史参与活动列表成功",
  "data": [
    {
      "activityId": 98,
      "title": "社区植树活动",
      "organizerName": "西湖社区居委会",
      "signupEndTimeAndActivityStartTime": "2024-11-12T19:00:00",
      "activityEndTime": "2024-11-12T22:00:00",
      "status": "history",
      "imageUrl": "https://cdn.example.com/activities/tree-planting.jpg",
      "location": "社区花园",
      "participantCount": 23,
      "maxParticipants": 25,
      "avatars": [
        "https://example.com/avatar6.jpg",
        "https://example.com/avatar7.jpg",
        "https://example.com/avatar8.jpg"
      ]
    }
  ]
}
```

#### 错误响应

```json
{
  "success": false,
  "message": "获取历史参与活动列表失败: 用户权限不足",
  "data": null
}
```

---

## 📊 数据模型

### ActivityListResponse 响应对象

| 字段名 | 类型 | 说明 |
|--------|------|------|
| activityId | Integer | 活动ID |
| title | String | 活动标题 |
| organizerName | String | 组织者名称（居委会名称） |
| signupEndTimeAndActivityStartTime | String | 报名结束时间/活动开始时间 |
| activityEndTime | String | 活动结束时间 |
| status | String | 活动状态：joined/available/history |
| imageUrl | String | 活动图片URL |
| location | String | 活动地点 |
| participantCount | Integer | 当前参与人数 |
| maxParticipants | Integer | 最大参与人数（可为null，表示无限制） |
| avatars | Array | 最近参与者头像URL列表（最多3个） |

### ApiResponse 标准响应格式

| 字段名 | 类型 | 说明 |
|--------|------|------|
| success | Boolean | 请求是否成功 |
| message | String | 响应消息 |
| data | Object/Array | 响应数据，失败时为null |

---

## 🔧 使用示例

### JavaScript/Axios 示例

```javascript
// 获取用户已报名的活动
const getJoinedActivities = async (userId) => {
  try {
    const response = await axios.get(`/api/user/${userId}/activities/joined`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    if (response.data.success) {
      console.log('已报名活动:', response.data.data);
      return response.data.data;
    } else {
      console.error('获取失败:', response.data.message);
    }
  } catch (error) {
    console.error('请求异常:', error);
  }
};

// 获取用户可报名的活动
const getAvailableActivities = async (userId) => {
  try {
    const response = await axios.get(`/api/user/${userId}/activities/available`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    return response.data;
  } catch (error) {
    console.error('请求失败:', error);
  }
};

// 获取用户历史参与的活动
const getHistoryActivities = async (userId) => {
  try {
    const response = await axios.get(`/api/user/${userId}/activities/history`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    return response.data;
  } catch (error) {
    console.error('请求失败:', error);
  }
};
```

### Java/Spring Boot 客户端示例

```java
@RestController
@RequestMapping("/client")
public class ActivityClientController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    // 获取用户已报名活动
    @GetMapping("/user/{userId}/joined")
    public ApiResponse<List<ActivityListResponse>> getJoinedActivities(
            @PathVariable Integer userId,
            @RequestHeader("Authorization") String token) {
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        String url = "http://localhost:8080/api/user/" + userId + "/activities/joined";
        
        return restTemplate.exchange(
            url, 
            HttpMethod.GET, 
            entity, 
            new ParameterizedTypeReference<ApiResponse<List<ActivityListResponse>>>() {}
        ).getBody();
    }
}
```

---

## ⚠️ 注意事项

### 1. **权限控制**
- 用户只能查询自己的活动信息
- 管理员和居委会可以查询任意用户的活动信息
- 请确保JWT Token有效且未过期

### 2. **数据一致性**
- `participantCount` 是实时计算的，反映当前实际参与人数
- `avatars` 显示最近报名的3位用户头像
- 活动状态会根据时间和参与情况动态变化

### 3. **性能考虑**
- 接口支持大量数据查询，但建议客户端实现分页或懒加载
- 头像URL建议使用CDN加速
- 建议客户端缓存活动列表，定期刷新

### 4. **时间格式**
- 所有时间字段采用ISO 8601格式：`YYYY-MM-DDTHH:mm:ss`
- 时区默认为服务器本地时区
- 前端显示时请根据用户时区进行转换

---

## 🐛 常见问题

### Q1: 为什么某些活动不在可报名列表中？
**A**: 可能的原因：
- 活动报名时间已过
- 活动已满员
- 用户已经报名该活动
- 活动状态不是 'published'

### Q2: 如何判断活动是否满员？
**A**: 当 `maxParticipants` 不为null且 `participantCount >= maxParticipants` 时，活动满员。
如果 `maxParticipants` 为null，表示无人数限制。

### Q3: avatars 字段为空是什么原因？
**A**: 可能的原因：
- 该活动还没有用户报名
- 参与用户没有设置头像
- 数据库查询异常（会使用默认头像）

### Q4: 同一个活动可能同时出现在多个列表中吗？
**A**: 不会。系统严格按照业务逻辑分类：
- joined：已报名但未完成参与
- available：可以报名（用户未报名）  
- history：已完成参与

---

## 📝 更新日志

| 版本 | 日期 | 更新内容 |
|------|------|----------|
| v1.0 | 2025-01-27 | 初始版本，包含三个基础查询接口 |

---

**开发团队**: 无废技术组  
**最后更新**: 2025-01-27  
**联系方式**: 如有问题请联系项目维护者 