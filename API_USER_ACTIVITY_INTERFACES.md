# 用户活动管理接口文档

## 📋 接口概述

本文档描述了**用户活动管理模块**的三个核心接口，用于普通用户查询活动参与情况。系统根据用户与活动的关系，将活动分为三种状态：

- **已报名活动**（joined）：用户已报名但尚未参与完成的活动
- **可报名活动**（available）：用户当前可以报名参与的活动
- **历史参与活动**（history）：用户已实际参与完成的活动

---

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
