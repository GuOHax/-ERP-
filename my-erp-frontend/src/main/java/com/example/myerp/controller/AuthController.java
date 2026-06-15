// my-erp-frontend/src/main/java/com/example/myerp/controller/AuthController.java
package com.example.myerp.controller;

import com.example.myerp.common.Result;
import com.example.myerp.entity.Employee;
import com.example.myerp.service.AuthService;
import com.example.myerp.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 登录接口
     * POST /api/auth/login
     * RequestBody: { "emNo": "...", "emPwd": "..." }
     */
    @PostMapping("/login")
    public ResponseEntity<Result<Employee>> login(@RequestBody LoginDTO loginDTO) {
        Employee employee = authService.authenticate(loginDTO.getEmNo(), loginDTO.getEmPwd());
        if (employee != null) {
            return ResponseEntity.ok(Result.success(employee, "登录成功"));
        } else {
            return ResponseEntity.ok(Result.error("用户名或密码错误"));
        }
    }

    /**
     * 注册接口（可选，用于员工自助注册）
     * POST /api/auth/register
     * RequestBody: Employee 对象
     */
    @PostMapping("/register")
    public ResponseEntity<Result<Employee>> register(@RequestBody Employee employee) {
        Employee saved = authService.register(employee);
        if (saved != null) {
            return ResponseEntity.ok(Result.success(saved, "注册成功"));
        } else {
            return ResponseEntity.ok(Result.error("注册失败：编号已存在"));
        }
    }
}
