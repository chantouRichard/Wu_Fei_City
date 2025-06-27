package com.whu.wufeibackend.controller;

import com.whu.wufeibackend.dto.ApiResponse;
import com.whu.wufeibackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("update")
    public ResponseEntity<ApiResponse<String>> update(@RequestBody Map<String, Object> requestBody) {
        try {
            String userIdStr = (String) requestBody.get("userId");

            if(userIdStr == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(400, "参数不完整", null, false));
            }

            Integer userId = Integer.parseInt(userIdStr);
            String nickname = (String) requestBody.get("nickname");
            String introduction = (String) requestBody.get("introduction");
            String avatar  = (String) requestBody.get("avatat");

            boolean success = userService.updateUserInfo(userId, nickname, introduction, avatar);

            if(success) {
                return ResponseEntity.ok(new ApiResponse<>(200, "用户信息更新成功", null, true));
            } else{
                return ResponseEntity.status(500)
                        .body(new ApiResponse<>(500, "用户信息更新失败", null, false));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500, "服务器内部错误", null, false));
        }
    }
}
