// my-erp-frontend/src/main/java/com/example/myerp/controller/RoleController.java
package com.example.myerp.controller;

import com.example.myerp.common.Result;
import com.example.myerp.entity.Role;
import com.example.myerp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/add")
    public Result<Role> add(@RequestBody Role role) {
        Role saved = roleService.save(role);
        return saved != null
            ? Result.success(saved, "添加角色成功")
            : Result.error("添加角色失败");
    }

    @PutMapping("/update/{id}")
    public Result<Role> update(@PathVariable Long id, @RequestBody Role role) {
        Role updated = roleService.update(id, role);
        return updated != null
            ? Result.success(updated, "更新角色成功")
            : Result.error("更新角色失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return roleService.delete(id)
            ? Result.success(null, "删除角色成功")
            : Result.error("删除角色失败");
    }

    @GetMapping("/list")
    public Result<List<Role>> list() {
        return Result.success(roleService.findAll(), "查询成功");
    }

    @GetMapping("/get/{id}")
    public Result<Role> get(@PathVariable Long id) {
        Role role = roleService.findById(id);
        return role != null
            ? Result.success(role, "查询成功")
            : Result.error("未找到该角色");
    }
}
