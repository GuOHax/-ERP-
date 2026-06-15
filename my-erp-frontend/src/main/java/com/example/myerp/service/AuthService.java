// my-erp-frontend/src/main/java/com/example/myerp/service/AuthService.java
package com.example.myerp.service;

import com.example.myerp.entity.Employee;

public interface AuthService {
    /**
     * 用户登录验证
     * @param emNo   员工编号
     * @param emPwd  密码
     * @return       成功返回 Employee 对象，失败返回 null
     */
    Employee authenticate(String emNo, String emPwd);

    /**
     * 用户注册（如果需要）
     * @param employee  包含 emNo、emPwd、emName 等字段
     * @return          注册成功返回保存后的 Employee，若 emNo 已存在返回 null
     */
    Employee register(Employee employee);
}
