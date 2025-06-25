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
import java.util.Arrays;
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
        System.out.println("【登录调试】前端密码：" + request.getPassword());
        System.out.println("【登录调试】数据库哈希：" + user.getPassword());
        System.out.println("【登录调试】userType：" + user.getUserType());
        System.out.println("【调试】前端密码原始字节：" + Arrays.toString(request.getPassword().getBytes()));
        System.out.println("【调试】数据库哈希原始字节：" + Arrays.toString(user.getPassword().getBytes()));

        // 验证密码
        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        System.out.println("【登录调试】BCrypt密码比对结果：" + passwordMatch);

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
            user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
            user.setIntroduction(request.getIntroduction() != null ? request.getIntroduction() : "这个人很懒，什么都没有留下~");
            user.setAvatar(request.getAvatar() != null ? request.getAvatar() : "https://via.placeholder.com/100/09f/fff.png");
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