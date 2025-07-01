package com.whu.wufeibackend.service;

import com.whu.wufeibackend.DTO.LoginResponse;
import com.whu.wufeibackend.DTO.SimpleLoginRequest;
import com.whu.wufeibackend.DTO.SimpleRegisterRequest;
import com.whu.wufeibackend.DTO.SimpleRegisterResponse;
import com.whu.wufeibackend.DTO.RankItem;
import com.whu.wufeibackend.entity.User;
import com.whu.wufeibackend.mapper.UserMapper;
import com.whu.wufeibackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务类
 * 处理用户注册、登录、审批等业务逻辑
 * 
 * @author 无废技术组
 * @since 2024-06-23
 */
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private GreenScoreService greenScoreService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // BCrypt密码编码器
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * 统一用户登录方法
     * 支持所有用户类型的登录（普通用户、居委会、管理员）
     * 
     * @param request 登录请求
     * @return 登录响应
     */
    public LoginResponse login(SimpleLoginRequest request) {
        // 根据用户名和用户类型查询用户
        User user = userMapper.findByUsernameAndUserType(request.getUsername(), request.getUserType());
        
        // 检查用户是否存在
        if (user == null) {
            System.out.println("【登录调试】未找到用户：" + request.getUsername() + "，类型：" + request.getUserType());
            return new LoginResponse(false, "账号不存在");
        }

        // 日志：打印密码明文和数据库哈希
//        System.out.println("【登录调试】前端密码：" + request.getPassword());
//        System.out.println("【登录调试】数据库哈希：" + user.getPassword());
//        System.out.println("【登录调试】userType：" + user.getUserType());
//        System.out.println("【调试】前端密码原始字节：" + Arrays.toString(request.getPassword().getBytes()));
//        System.out.println("【调试】数据库哈希原始字节：" + Arrays.toString(user.getPassword().getBytes()));

        // 验证密码
        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
//        System.out.println("【登录调试】BCrypt密码比对结果：" + passwordMatch);

        if (!passwordMatch) {
            return new LoginResponse(false, "密码错误");
        }
        
        // 检查居委会审批状态
        if ("committee".equals(request.getUserType())) {
            if (user.getApproved() == null) {
                return new LoginResponse(false, "账号未审批");
            } else if (user.getApproved() == 0) {
                return new LoginResponse(false, "账号审批被拒绝");
            }
        }
        
        // 构建登录响应
        boolean isAdmin = "admin".equals(request.getUserType());
        return buildLoginResponse(user, isAdmin);
    }
    
    /**
     * 构建登录响应
     * 
     * @param user 用户对象
     * @param isAdmin 是否为管理员
     * @return 登录响应
     */
    private LoginResponse buildLoginResponse(User user, boolean isAdmin) {
        LoginResponse response = new LoginResponse(true);
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setUserType(user.getUserType());
        response.setNickname(user.getNickname());
        response.setIntroduction(user.getIntroduction());
        response.setAvatar(user.getAvatar());
        response.setCommitteeDesc(user.getCommitteeDesc());
        response.setContact(user.getContact());
        
        // 只有管理员登录时才生成JWT令牌
        if (isAdmin) {
            String token = jwtUtil.generateToken(user.getUsername(), user.getUserType());
            response.setToken(token);
        }
        
        // 为普通用户添加额外字段（从数据库获取真实数据）
        if ("normal".equals(user.getUserType())) {
            try {
                // 设置绿色积分（从数据库获取用户真实积分）
                Integer userTotalScore = greenScoreService.getUserTotalScore(user.getId());
                response.setGreenScore(userTotalScore);
                
                // 设置历史分数档次数据，当月的热力图
                List<Integer> history = greenScoreService.getUserScoreHistory(user.getId());
                response.setHistory(history);
                
                // 设置活动参与次数（从数据库获取）
                Integer activityCount = greenScoreService.getUserActivityParticipationCount(user.getId());
                response.setActivityParticipationNum(activityCount);
                
                // 设置排行榜数据（前5名真实数据）
                List<RankItem> rankList = greenScoreService.getTop5Ranking();
                response.setRank(rankList);
                
            } catch (Exception e) {
                // 如果查询失败，使用默认值
                e.printStackTrace();
                int daysInMonth = getDaysInCurrentMonth();
                response.setGreenScore(0);

                // 构建与当月天数一致的默认 history 数组
                List<Integer> defaultHistory = new ArrayList<>();
                for (int i = 0; i < daysInMonth; i++) {
                    defaultHistory.add(0);
                }
                response.setHistory(defaultHistory);

                response.setActivityParticipationNum(0);
                response.setRank(new ArrayList<>());
            }
        }
        
        return response;
    }
    
    /**
     * 统一用户注册方法
     * 支持所有用户类型的注册（普通用户、居委会）
     * 
     * @param request 注册请求
     * @return 注册响应
     */
    public SimpleRegisterResponse register(SimpleRegisterRequest request) {
        // 检查用户名是否已存在
        if (userMapper.countByUsername(request.getUsername()) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建用户对象
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserType(request.getUserType());
        
        // 根据用户类型设置字段
        if (request.isSimpleRegister()) {
            // 普通用户注册 - 使用默认值
            user.setNickname(request.getNickname() != null ? request.getNickname() : "默认用户名");
            user.setIntroduction(request.getIntroduction() != null ? request.getIntroduction() : "这个人很懒，什么都没有留下~");
            user.setAvatar(request.getAvatar() != null ? request.getAvatar() : "data:image/png;base64,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");
            user.setPending(0); // 不需要审批
            user.setApproved(1); // 默认通过
        } else if (request.isCommitteeRegister()) {
            // 居委会注册 - 使用传入的值
            user.setNickname(request.getNickname());
            user.setIntroduction(request.getIntroduction());
            user.setAvatar(request.getAvatar());
            user.setCommitteeDesc(request.getCommitteeDesc());
            user.setContact(request.getContact());
            user.setPending(1); // 需要审批
            user.setApproved(null); // 审批状态为null
        }
        
        // 保存用户
        userMapper.insertUser(user);
        
        // 返回注册响应
        String message = request.isSimpleRegister() ? "注册成功" : "居委会注册成功，请等待管理员审批";
        return new SimpleRegisterResponse(message, user.getUsername(), user.getUserType());
    }
    
    /**
     * 获取待审批的居委会用户列表
     * 
     * @return 待审批用户列表
     */
    public List<User> getPendingCommitteeUsers() {
        return userMapper.findPendingCommitteeUsers();
    }
    
    /**
     * 审批居委会用户
     * 
     * @param userId 用户ID
     * @param approved 是否通过
     * @return 是否成功
     */
    public boolean approveCommitteeUser(Integer userId, boolean approved) {
        User user = userMapper.findById(userId);
        if (user == null || !"committee".equals(user.getUserType())) {
            return false;
        }
        
        user.setPending(0); // 不再待审批
        user.setApproved(approved ? 1 : 0); // 设置审批结果
        
        return userMapper.updateUser(user) > 0;
    }
    
    /**
     * 验证JWT令牌
     * 
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        System.out.println("【JWT调试】开始校验token: " + token);
        boolean valid = jwtUtil.validateToken(token);
        System.out.println("【JWT调试】token校验结果: " + valid);
        return valid;
    }
    
    /**
     * 从JWT令牌中获取用户名
     * 
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }
    
    /**
     * 从JWT令牌中获取用户类型
     * 
     * @param token JWT令牌
     * @return 用户类型
     */
    public String getUserTypeFromToken(String token) {
        return jwtUtil.getUserTypeFromToken(token);
    }
    


    /**
     * 根据用户ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    public User getUserById(Integer userId) {
        return userMapper.findById(userId);
    }

    public boolean updateUserInfo(int userId, String nickName, String introduction, String avatar){
        User user = userMapper.findById(userId);

        if (user == null) return false;
        user.setNickname(nickName);
        user.setIntroduction(introduction);
        user.setAvatar(avatar);

        return userMapper.updateUser(user) == 1;
    }

    private int getDaysInCurrentMonth() {
        return LocalDate.now().lengthOfMonth();
    }
} 