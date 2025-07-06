package com.whu.wufeibackend.mapper;

import com.whu.wufeibackend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 * 定义用户相关的数据库操作
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);
    
    /**
     * 根据用户名和用户类型查询用户
     * 
     * @param username 用户名
     * @param userType 用户类型
     * @return 用户对象
     */
    User findByUsernameAndUserType(@Param("username") String username, @Param("userType") String userType);
    
    /**
     * 插入新用户
     * 
     * @param user 用户对象
     * @return 影响的行数
     */
    int insertUser(User user);
    
    /**
     * 更新用户信息
     * 
     * @param user 用户对象
     * @return 影响的行数
     */
    int updateUser(User user);
    
    /**
     * 查询所有待审批的居委会用户
     * 
     * @return 待审批用户列表
     */
    List<User> findPendingCommitteeUsers();

    List<User> getApprovedCommitteeUsers();

    List<User> getRefusedCommitteeUsers();
    
    /**
     * 根据用户ID查询用户
     * 
     * @param id 用户ID
     * @return 用户对象
     */
    User findById(Integer id);
    
    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    int countByUsername(String username);
} 