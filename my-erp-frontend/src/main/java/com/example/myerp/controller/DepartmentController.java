// my-erp-frontend/src/main/java/com/example/myerp/controller/DepartmentController.java
package com.example.myerp.controller;

import com.example.myerp.common.Result;
import com.example.myerp.entity.Department;
import com.example.myerp.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 新增根部门
     * POST /api/department/add
     */
    @PostMapping("/add")
    public Result<Department> addDepartment(@RequestBody Department department) {
        Department saved = departmentService.save(department);
        if (saved != null) {
            return Result.success(saved, "添加部门成功");
        } else {
            return Result.error("添加部门失败");
        }
    }

    /**
     * 更新部门
     * PUT /api/department/update/{id}
     */
    @PutMapping("/update/{id}")
    public Result<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Department updated = departmentService.update(id, department);
        if (updated != null) {
            return Result.success(updated, "更新部门成功");
        } else {
            return Result.error("更新部门失败");
        }
    }

    /**
     * 删除部门
     * DELETE /api/department/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteDepartment(@PathVariable Long id) {
        boolean ok = departmentService.delete(id);
        if (ok) {
            return Result.success(null, "删除部门成功");
        } else {
            return Result.error("删除部门失败");
        }
    }

    /**
     * 切换部门显示状态
     * PUT /api/department/toggle/{id}
     */
    @PutMapping("/toggle/{id}")
    public Result<Department> toggleDepartmentShow(@PathVariable Long id) {
        Department dept = departmentService.toggleShow(id);
        if (dept != null) {
            return Result.success(dept, "切换显示成功");
        } else {
            return Result.error("切换显示失败");
        }
    }

    /**
     * 查询所有部门
     * GET /api/department/list
     */
    @GetMapping("/list")
    public Result<List<Department>> listDepartments() {
        List<Department> list = departmentService.findAll();
        return Result.success(list, "查询部门成功");
    }

    /**
     * 根据 ID 查询部门
     * GET /api/department/get/{id}
     */
    @GetMapping("/get/{id}")
    public Result<Department> getDepartment(@PathVariable Long id) {
        Department dept = departmentService.findById(id);
        if (dept != null) {
            return Result.success(dept, "查询成功");
        } else {
            return Result.error("未找到该部门");
        }
    }

    /**
     * 添加子部门
     * POST /api/department/addChild/{parentId}
     */
    @PostMapping("/addChild/{parentId}")
    public Result<Department> addChild(@PathVariable Long parentId, @RequestBody Department department) {
        Department saved = departmentService.addChild(parentId, department);
        if (saved != null) {
            return Result.success(saved, "添加子部门成功");
        } else {
            return Result.error("添加子部门失败");
        }
    }
}
