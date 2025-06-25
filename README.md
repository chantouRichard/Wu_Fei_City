# 无废技术组 - 后端服务

## 项目简介

无废技术组后端服务，基于Spring Boot + MyBatis + MySQL技术栈，提供用户认证和管理功能。

## 技术栈

- **Java**: 17
- **Spring Boot**: 3.5.3
- **MyBatis**: 3.0.3
- **MySQL**: 8.0+
- **JWT**: 0.11.5
- **BCrypt**: 密码加密

## 功能特性

### 用户管理
- 普通用户注册和登录
- 居委会注册和登录（需要审批）
- 管理员登录（预分配账号）
- JWT令牌认证（仅管理员）

### 审批系统
- 居委会注册申请审批
- 待审批用户列表查看
- 审批状态管理

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE wu_fei_city DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本：
```bash
mysql -u root -p wu_fei_city < src/main/resources/sql/init.sql
```

### 配置文件

修改 `src/main/resources/application.properties` 中的数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wu_fei_city?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 运行项目

1. 克隆项目：
```bash
git clone <repository-url>
cd Wu_Fei_City
```

2. 编译项目：
```bash
mvn clean compile
```

3. 运行项目：
```bash
mvn spring-boot:run
```

或者使用IDE直接运行 `WuFeiBackendApplication.java`

### 访问地址

- 应用地址：http://localhost:8080
- API基础地址：http://localhost:8080/api

## API文档

详细的API接口文档请参考：[API_DOCUMENTATION.md](API_DOCUMENTATION.md)

### 主要接口

- `POST /api/user/register` - 普通用户注册
- `POST /api/user/login` - 普通用户登录
- `POST /api/committee/register` - 居委会注册
- `POST /api/committee/login` - 居委会登录
- `POST /api/admin/login` - 管理员登录
- `GET /api/admin/pending-users` - 获取待审批用户列表
- `POST /api/admin/approve` - 审批用户

## 默认账号

### 管理员账号
- 用户名：admin
- 密码：admin123
- 用户类型：admin

## 项目结构

```
src/main/java/com/whu/wufeibackend/
├── config/                 # 配置类
├── controller/             # 控制器
│   ├── AuthController.java     # 认证控制器
│   └── AdminController.java    # 管理员控制器
├── DTO/                   # 数据传输对象
│   ├── ApiResponse.java        # 统一响应格式
│   ├── LoginRequest.java       # 登录请求
│   ├── LoginResponse.java      # 登录响应
│   └── RegisterRequest.java    # 注册请求
├── entity/                # 实体类
│   └── User.java              # 用户实体
├── exception/             # 异常处理
│   └── GlobalExceptionHandler.java
├── mapper/                # MyBatis映射器
│   └── UserMapper.java        # 用户映射器
├── service/               # 服务层
│   └── UserService.java       # 用户服务
├── util/                  # 工具类
│   └── JwtUtil.java           # JWT工具
└── WuFeiBackendApplication.java  # 启动类
```

## 开发指南

### 添加新功能

1. 在 `entity` 包中创建实体类
2. 在 `mapper` 包中创建Mapper接口
3. 在 `resources/mapper` 目录下创建XML映射文件
4. 在 `service` 包中创建服务类
5. 在 `controller` 包中创建控制器
6. 在 `DTO` 包中创建请求/响应对象

### 代码规范

- 使用Java 17语法特性
- 遵循Spring Boot最佳实践
- 使用MyBatis注解和XML混合配置
- 统一使用ApiResponse响应格式
- 添加详细的代码注释

## 部署

### 打包

```bash
mvn clean package
```

### 运行JAR文件

```bash
java -jar target/WuFei-backend-0.0.1-SNAPSHOT.jar
```

### Docker部署

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/WuFei-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 测试

### 单元测试

```bash
mvn test
```

### 接口测试

可以使用Postman或其他API测试工具测试接口，参考API文档中的示例。

## 常见问题

### 1. 数据库连接失败
- 检查MySQL服务是否启动
- 验证数据库连接配置
- 确认数据库用户权限

### 2. JWT令牌无效
- 检查JWT密钥配置
- 确认令牌格式正确
- 验证令牌是否过期

### 3. 跨域问题
- 项目已配置CORS支持
- 前端请求需要设置正确的Content-Type

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交代码
4. 创建Pull Request

## 许可证

本项目采用MIT许可证，详见LICENSE文件。

## 联系方式

如有问题或建议，请联系项目维护者。
